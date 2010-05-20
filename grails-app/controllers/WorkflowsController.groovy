class WorkflowsController {
	def securityService
	def savedAnalysisService
	def userListService
	def middlewareService
	def quickStartService
	
    def index = { 
		if(!session.profileLoaded){
			def studyNames = securityService.getSharedItemIds(session.userId, StudyDataSource.class.name)
			println studyNames
			def myStudies = []
			studyNames.each{
				def foundStudy = StudyDataSource.findByShortName(it)
				if(foundStudy){
					myStudies << foundStudy
				}
			}
			session.myStudies = myStudies
			def myCollaborationGroups = securityService.getCollaborationGroups(session.userId)
			def sharedListIds = userListService.getSharedListIds(session.userId)
			session.sharedListIds = sharedListIds
			//get shared anaylysis and places them in session scope
			def sharedAnalysisIds = savedAnalysisService.getSharedAnalysisIds(session.userId)
			session.sharedAnalysisIds = sharedAnalysisIds
			session.dataAvailability = quickStartService.getDataAvailability(session.myStudies)
			
			session.myCollaborationGroups = myCollaborationGroups
		
			loadRemoteSources()
			session.profileLoaded = true
			println session.myCollaborationGroups
		}
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
