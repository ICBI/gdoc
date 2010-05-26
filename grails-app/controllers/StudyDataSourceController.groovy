import grails.converters.*

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
		def studyNames = securityService.getSharedItemIds(session.userId, StudyDataSource.class.name)
		println studyNames
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
		println myStudies
		if(myStudies.metaClass.respondsTo(myStudies, "size")) {
			otherStudies.removeAll(myStudies)
		} else {
			otherStudies.remove(myStudies)
		}
		
	}
	
	def setStudy = {
		if(params.study){
			println "set study to $params.study"
			def currStudy = StudyDataSource.get(params.study)
			session.study = currStudy
			StudyContext.setStudy(session.study.schemaName)
			session.dataTypes = AttributeType.findAll().sort { it.longName }
			def lists = userListService.getAllLists(session.userId,session.sharedListIds)
			def patientLists = []
			def reporterLists = []
			def geneLists = []
			patientLists = lists.findAll { item ->
				(item.tags.contains("patient") && item.schemaNames().contains(StudyContext.getStudy()))
			}
			reporterLists = lists.findAll { item ->
				(item.tags.contains("Reporter"))
			}
			geneLists = lists.findAll { item ->
				(item.tags.contains("gene"))
			}
			session.patientLists = patientLists.sort { it.name }
			session.reporterLists = reporterLists
			session.geneLists = geneLists
			session.endpoints = KmAttribute.findAll()
			session.files = htDataService.getHTDataMap()
			session.dataSetType = session.files.keySet()
			render session.study.shortName
		}
		else render ""
	}
	
	def findStudiesForDisease = {
		def myStudies = []
		def studiesJSON = []
		
		if(params.disease){
			println "user interested in $params.disease, grab all studies that have data for $params.disease"
			myStudies = session.myStudies.findAll{it.cancerSite == params.disease}
			myStudies.each{
				if(it.shortName!="DRUG"){7
					def studies = [:]
					studies["studyName"] = it.shortName
					studies["studyId"] = it.id
					studiesJSON << studies
				}
			}
		}
		render studiesJSON as JSON 
	}
	
	def show = {
		def currStudy = StudyDataSource.get(params.id)
		session.study = currStudy
		StudyContext.setStudy(session.study.schemaName)
		if(currStudy.hasClinicalData()){
			clinicalElements = AttributeType.findAll()
		}
		println clinicalElements
	}
	
	def loadRemoteSources() {
		def middlewareSources = middlewareService.loadResource("Datasource", null, session.userId)
		def dataSourceMap = [:]
		println middlewareSources
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
		println dataSourceMap
		session.dataSourceMap = dataSourceMap
	}
}
