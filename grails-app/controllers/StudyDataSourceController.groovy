class StudyDataSourceController {

	def myStudies
	def otherStudies
	def clinicalElements
	def securityService
	def savedAnalysisService
	def userListService
	def middlewareService
	
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
