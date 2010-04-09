class HtDataService {

    boolean transactional = true
	
	def jdbcTemplate
	
	private static def HT_FILE_INSERT = 'insert into ${schemaName}.HT_FILE(HT_FILE_ID, NAME, RELATIVE_PATH, SIZE_B, FILE_TYPE_ID, FILE_FORMAT_ID, DATA_LEVEL, INSERT_USER, INSERT_DATE, INSERT_METHOD, DESCRIPTION) ' +
																			'values (${schemaName}.HT_FILE_SEQUENCE.nextVal, \'${fileName}\', \'${relativePath}\', ${fileSize}, ${fileTypeId}, ${fileFormatId}, \'${dataLevel}\', \'${insertUser}\', (SELECT SYSDATE FROM dual), \'${insertMethod}\', \'${description}\')'
	private static def FILE_TYPE_SELECT = 'select FILE_TYPE_ID from FILE_TYPE where NAME = \'${fileType}\''
	
	private static def FILE_FORMAT_SELECT = 'select FILE_FORMAT_ID from FILE_FORMAT where NAME = \'${fileFormat}\''
	
	private static def LINK_BIOSPECIMEN_RUN = 'insert into ${schemaName}.HT_RUN_BIOSPECIMEN(HT_RUN_BIOSPECIMEN_ID, BIOSPECIMEN_ID, HT_RUN_ID) values (${schemaName}.HT_RUN_BIOSPECIMEN_SEQUENCE.nextval, ${biospecimenId}, ${runId})'
    def loadHtFile(params, priorFiles) {
	
/*		def engine = new groovy.text.SimpleTemplateEngine() 
		def fileTypeTemplate = engine.createTemplate(FILE_TYPE_SELECT)
		def fileTypeQuery = fileTypeTemplate.make(params)
		def fileTypeId = jdbcTemplate.queryForLong(fileTypeQuery.toString())
		params['fileTypeId'] = fileTypeId
		
		def fileFormatTemplate = engine.createTemplate(FILE_FORMAT_SELECT)
		def fileFormatQuery = fileFormatTemplate.make(params)
		def fileFormatId = jdbcTemplate.queryForLong(fileFormatQuery.toString())
		params['fileFormatId'] = fileFormatId
		
		def template = engine.createTemplate(HT_FILE_INSERT) 
		Writable writable = template.make(params)
		jdbcTemplate.execute(writable.toString())*/
		StudyContext.setStudy(params.schemaName)
		def fileType = FileType.findByName(params.fileType)
		def fileFormat = FileFormat.findByName(params.fileFormat)
		def file = new MicroarrayFile(params)
		file.fileType = fileType
		file.fileFormat = fileFormat
		if(priorFiles)
			file.priorFiles = priorFiles
		if(!file.save())
			println file.errors
		return file
	}
	
	def loadHtRun(params, file) {
		def design = HtDesign.findByPlatform(params.design)
		if(!design) {
			throw new Exception("HtDesign cannot be null")
		}
		StudyContext.setStudy(params.schemaName)
		def run = new HtRun(params)
		run.design = design
		run.rawFile = file
		if(!run.save()) 
			println run.errors
		return run
	}
	
	def loadNormalizedFileWithPriors(priorFiles, normalizedFile) {
		def files = []
		priorFiles.each {
			def file = loadHtFile(it, null)
			def run = loadHtRun(it, file)
			def biospecimen = loadBiospecimen(it)
			linkBiospecimenRun(it.schemaName, biospecimen.id, run.id)
			files << file
		}
		def normFile = loadHtFile(normalizedFile, files)
		return normFile
	}
	
	def loadBiospecimen(params) {
		
		def studyPatient = StudyPatient.findByDataSourceInternalId(params.patientId)
		def patient  = Patient.get(studyPatient.id)
		def biospecimen = new Biospecimen(params)
		biospecimen.patient = patient
		biospecimen.type = "SAMPLE"
		biospecimen.diseased = true
		biospecimen.attributeTimepointId = 0
		if(!biospecimen.save())
			println biospecimen.errors
		return biospecimen
	}
	
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
	
	
}
