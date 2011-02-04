class HtDataService {

    boolean transactional = true
	
	def jdbcTemplate
	def biospecimenService
	private static def HT_FILE_INSERT = 'insert into ${schemaName}.HT_FILE(HT_FILE_ID, NAME, RELATIVE_PATH, SIZE_B, FILE_TYPE_ID, FILE_FORMAT_ID, DATA_LEVEL, INSERT_USER, INSERT_DATE, INSERT_METHOD, DESCRIPTION) ' +
																			'values (${schemaName}.HT_FILE_SEQUENCE.nextVal, \'${fileName}\', \'${relativePath}\', ${fileSize}, ${fileTypeId}, ${fileFormatId}, \'${dataLevel}\', \'${insertUser}\', (SELECT SYSDATE FROM dual), \'${insertMethod}\', \'${description}\')'
	private static def FILE_TYPE_SELECT = 'select FILE_TYPE_ID from FILE_TYPE where NAME = \'${fileType}\''
	
	private static def FILE_FORMAT_SELECT = 'select FILE_FORMAT_ID from FILE_FORMAT where NAME = \'${fileFormat}\''
	
	private static def LINK_BIOSPECIMEN_RUN = 'insert into ${schemaName}.HT_RUN_BIOSPECIMEN(HT_RUN_BIOSPECIMEN_ID, BIOSPECIMEN_ID, HT_RUN_ID) values (${schemaName}.HT_RUN_BIOSPECIMEN_SEQUENCE.nextval, ${biospecimenId}, ${runId})'
    def loadHtFile(params, priorFiles) {
	
		StudyContext.setStudy(params.schemaName)
		def fileType = FileType.findByName(params.fileType)
		def fileFormat = FileFormat.findByName(params.fileFormat)
		def file = new MicroarrayFile(params)
		file.fileType = fileType
		file.fileFormat = fileFormat
		if(params.fileName)
			file.name = params.fileName
		if(priorFiles)
			file.priorFiles = priorFiles
		if(!file.save())
			log.debug file.errors
		return file
	}
	
	def loadHtRun(params, file) {
		def design = Design.findByPlatform(params.design)
		if(!design) {
			throw new Exception("HtDesign cannot be null")
		}
		StudyContext.setStudy(params.schemaName)
		def run = new HtRun(params)
		run.design = design
		run.rawFile = file
		run.name = params.fileName
		if(!run.save()) 
			log.debug run.errors
		return run
	}
	
	def loadNormalizedFileWithPriors(priorFiles, normalizedFile) {
		def files = []
		priorFiles.each {
			StudyContext.setStudy(it.schemaName)
			def file = MicroarrayFile.findByName(it.fileName)
			if(!file){
			  file = loadHtFile(it, null)
			}else{
			   log.debug "$file.name has already been loaded, just load the run"
			}
			def run = loadHtRun(it, file)
			log.debug "get biospecimen to link with run"
			def studyPatient = StudyPatient.findByDataSourceInternalId(it.patientId)
			def patient  = Patient.get(studyPatient.id)
			def biospecimen = Biospecimen.findByPatientAndName(patient, it.name)
			if(biospecimen){
				log.debug "found biospecimen, now link"
			}else{
				log.debug "biospecimen does not yet exist, create one, and then link"
				biospecimen = biospecimenService.loadBiospecimen(it)
			}
			
			linkBiospecimenRun(it.schemaName, biospecimen.id, run.id)
			files << file
		}
		def normFile = loadHtFile(normalizedFile, files)
		return normFile
	}
	
	/**def loadBiospecimen(params) {
		
		def studyPatient = StudyPatient.findByDataSourceInternalId(params.patientId)
		def patient  = Patient.get(studyPatient.id)
		def biospecimen = new Biospecimen(params)
		biospecimen.patient = patient
		biospecimen.type = "SAMPLE"
		biospecimen.diseased = true
		biospecimen.attributeTimepointId = 0
		if(!biospecimen.save())
			log.debug biospecimen.errors
		return biospecimen
	}**/
	
	def linkBiospecimenRun(schemaName, biospecimenId, runId) {
		def engine = new groovy.text.SimpleTemplateEngine() 
		def insertTemplate = engine.createTemplate(LINK_BIOSPECIMEN_RUN)
		def params = [:]
		params.biospecimenId = biospecimenId
		params.runId = runId
		params.schemaName = schemaName
		def insertStatement = insertTemplate.make(params)
		jdbcTemplate.execute(insertStatement.toString())
	}
	
	def getAllHTDataTypes(){
		def htTypes = []
		def arrayTypes = []
		htTypes << DataType.CLINICAL.value()
		def types = ArrayDesign.createCriteria().list()
			{
				projections{
					distinct('arrayType')
				}
			}
		types.each{
			htTypes << it
		}
		def mstypes = MSDesign.createCriteria().list()
			{
				projections{
					distinct('msType')
				}
			}
		mstypes.each{
			htTypes << it
		}
		return htTypes
	}
	
	
	def getHTDataMap() {
		//def files = Sample.executeQuery("select distinct s.file, s.designType from Sample s where s.designType != 'COPY_NUMBER'")
		def files = Sample.executeQuery("select distinct s.file, s.designType from Sample s")
		def fileHash = [:]
		files.each {
			if(!fileHash[it[1]])
				fileHash[it[1]] = []
			//if(it[1] == "GENE EXPRESSION")
			fileHash[it[1]] << MicroarrayFile.findByName(it[0])
				
		}
		return fileHash
	}
	
	def getGEDataMap() {
		def files = Sample.executeQuery("select distinct s.file, s.designType from Sample s where s.designType  = 'GENE EXPRESSION'")
		def fileHash = [:]
		files.each {
			if(!fileHash[it[1]])
				fileHash[it[1]] = []
			fileHash[it[1]] << MicroarrayFile.findByName(it[0])
				
		}
		return fileHash
	}
	
	def getCINDataMap() {
		def files = Sample.executeQuery("select distinct s.file, s.designType from Sample s where s.designType = 'COPY_NUMBER' order by s.file")
		def fileHash = [:]
		files.each {
			if(!fileHash[it[1]])
				fileHash[it[1]] = []
				fileHash[it[1]] << it[0]	
		}
		return fileHash
	}
	
	def loadPeaks(rdaFile, allPeaks) {
		allPeaks.each {
			StudyContext.setStudy(it.schemaName)
			def file = MicroarrayFile.findByName(rdaFile)
			if(!file){
				log.error "File $rdaFile has not been loaded.  Please check the mapping files."
				return
			}
			def peak = new MSPeak(it)
			peak.file = file
			if(!peak.save())
				log.debug peak.errors
		}
	}
	
	def loadArrayDesign(params) {
		def design = new ArrayDesign(params)
		if(!design.save())
			log.error design.errors
		return design
	}
	
	def loadReportersForDesign(platformName, reporters) {
		def design = ArrayDesign.findByPlatform(platformName)
		if(!design)
			throw new Exception("Platform not found")
		reporters.each {
			def reporter = new Reporter(it)
			reporter.addToArrayDesigns(design)
			design.addToReporters(reporter)
		}
		if(!design.save()) {
			log.error design.errors
			throw new Exception("Error saving reporters.")
		}
	}
}
