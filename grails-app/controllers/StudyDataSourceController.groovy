import grails.converters.*

@Mixin(ControllerMixin)
class StudyDataSourceController {

	def myStudies
	def otherStudies
	def clinicalElements
	def securityService
	def savedAnalysisService
	def userListService
	def middlewareService
	def htDataService
	
    def index = { 
		def studyNames = securityService.getSharedItemIds(session.userId, StudyDataSource.class.name,false)
		log.debug studyNames
		myStudies = []
		studyNames.each{
			def foundStudy = StudyDataSource.findByShortName(it)
			if(foundStudy){
				myStudies << foundStudy
			}
		}
		myStudies = myStudies.sort{ it.shortName }
		otherStudies = StudyDataSource.findAll()
		otherStudies.sort { it.shortName }
		log.debug myStudies
		if(myStudies.metaClass.respondsTo(myStudies, "size")) {
			otherStudies.removeAll(myStudies)
		} else {
			otherStudies.remove(myStudies)
		}
		
	}
	
	def setStudy = {
		if(params.study && session.myStudies){
			def studyid = new Long(params.study)
			def allowedStudyAccess = session.myStudies.find{it.id == studyid}
			if(allowedStudyAccess){
				log.debug "set study to $params.study"
				def currStudy = StudyDataSource.get(params.study)
				session.study = currStudy
				StudyContext.setStudy(session.study.schemaName)
				session.dataTypes = AttributeType.findAll().sort { it.longName }
				loadPatientLists()
				loadReporterLists()
				loadGeneLists()
				session.endpoints = KmAttribute.findAll()
				session.files = htDataService.getHTDataMap()
				session.dataSetType = session.files.keySet()
				render session.study.shortName
			}else{
				log.debug "user is NOT permitted to access this study"
				redirect(controller:'policies',action:'deniedAccess')
			}
		}
		else render ""
	}
	
	def selectDataType = {
		if(!session.files[params.dataType])
			render g.select(optionKey: 'name', optionValue: 'description', noSelection: ['': 'Select Data Type First'], id: 'dataFile', name: "dataFile")
		else
			render g.select(optionKey: 'name', optionValue: 'description', from: session.files[params.dataType].sort{it.name}, id: 'dataFile', name: "dataFile")
	}
	
	def findStudiesForDisease = {
		def myStudies = []
		def studiesJSON = []
		
		if(params.disease){
			log.debug "user interested in $params.disease, grab all studies that have data for $params.disease"
			myStudies = session.myStudies.findAll{it.cancerSite == params.disease}
			myStudies.each{
				if(it.shortName!="DRUG"){
					def studies = [:]
					studies["studyName"] = it.shortName
					studies["studyId"] = it.id
					studiesJSON << studies
				}
			}
		}
		studiesJSON.sort { it.studyName }
		render studiesJSON as JSON 
	}
	
	def show = {
		def currStudy = StudyDataSource.get(params.id)
		def allowAccess = false
		if(currStudy.hasClinicalData()){
			StudyContext.setStudy(currStudy.schemaName)
			clinicalElements = AttributeType.findAll()
			//log.debug clinicalElements
			if(session.myStudies){
				def mysid = session.myStudies.find{
					 it.id == params.id.toLong()
				}
				if(mysid){
					session.study = currStudy
					allowAccess = true
				}
			}
				
		}
		[currStudy:currStudy,clinicalElements:clinicalElements,allowAccess:allowAccess]
	}
	
	def loadRemoteSources() {
		def middlewareSources = middlewareService.loadResource("Datasource", null, session.userId)
		def dataSourceMap = [:]
		log.debug middlewareSources
		if(middlewareSources instanceof Map) {
			middlewareSources.each { key, value ->
				value.resources.each {
					if(!dataSourceMap[it]) {
						dataSourceMap[it] = []
					}
					dataSourceMap[it] << key
				}
			}
		}
		log.debug dataSourceMap
		session.dataSourceMap = dataSourceMap
	}
}
