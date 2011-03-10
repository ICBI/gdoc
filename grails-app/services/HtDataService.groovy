class HtDataService {

    boolean transactional = true
	
	def jdbcTemplate
	def biospecimenService
	
    def loadHtFile(params) {
	
		StudyContext.setStudy(params.schemaName)
		def design = Design.findByPlatform(params.design)
	   	if(!design) {
	   		throw new Exception("HtDesign cannot be null")
	   	}
		def file = new MicroarrayFile()
		file.design = design
		file.name = params.name
		file.description = params.description
		if(!file.save())
			log.debug file.errors
		return file
	}
	
	def loadFileAndSubjects(biospecimens, htFile) {
		StudyContext.setStudy(htFile.schemaName)
		println "LOADING HTFILE ${htFile} with biospecimens ${biospecimens}"
		def file = MicroarrayFile.findByName(htFile.name)
		if(!file){
		  file = loadHtFile(htFile)
		}else{
		   log.debug "$file.name has already been loaded, just load the run"
		}
		biospecimens.each {

			def studyPatient = StudyPatient.findByDataSourceInternalId(it.patientId)
			def patient  = Patient.get(studyPatient.id)
			def biospecimen = Biospecimen.findByPatientAndName(patient, it.name)
			if(biospecimen){
				log.debug "found biospecimen, now link"
			}else{
				log.debug "biospecimen does not yet exist, create one, and then link"
				biospecimen = biospecimenService.loadBiospecimen(it)
			}
			
			file.addToSubjects(biospecimen)
			if(!file.merge())
				log.error file.errors
		}
		return file
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
