class StudyDataSourceController {

	def myStudies
	def myCollaborationGroups
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
		myCollaborationGroups = securityService.getCollaborationGroups(session.userId)
		studyNames.each{
			def foundStudy = StudyDataSource.findByShortName(it)
			if(foundStudy){
				myStudies << foundStudy
			}
		}
		otherStudies = StudyDataSource.findAll()
		println myStudies
		if(myStudies.metaClass.respondsTo(myStudies, "size")) {
			otherStudies.removeAll(myStudies)
		} else {
			otherStudies.remove(myStudies)
		}
		//TODO -- move these session based variables out into security service or util class
		//get shared lists, and places them is session scope
		def sharedListIds = userListService.getSharedListIds(session.userId)
		session.sharedListIds = sharedListIds
		//get shared anaylysis and places them in session scope
		def sharedAnalysisIds = savedAnalysisService.getSharedAnalysisIds(session.userId)
		session.sharedAnalysisIds = sharedAnalysisIds
		
		session.myStudies = myStudies
		session.myCollaborationGroups = myCollaborationGroups
		
		loadRemoteSources()
		println session.myCollaborationGroups
	}
	
	def show = {
		def currStudy = StudyDataSource.get(params.id)
		session.study = currStudy
		clinicalElements = AttributeType.findAll()
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
