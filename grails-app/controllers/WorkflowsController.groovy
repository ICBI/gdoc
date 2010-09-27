class WorkflowsController {
	def securityService
	def savedAnalysisService
	def userListService
	def middlewareService
	def quickStartService
	def cleanupService
	
    def index = { 
		if(!session.profileLoaded){
			def studyNames = securityService.getSharedItemIds(session.userId, StudyDataSource.class.name)
			log.debug studyNames
			def myStudies = []
		
		    //cleanup any temporary artifacts left from last session
			cleanup(session.userId)
			session.tempLists = new HashSet()
			session.tempAnalyses = new HashSet()
			studyNames.each{
				def foundStudy = StudyDataSource.findByShortName(it)
				if(foundStudy){
					myStudies << foundStudy
				}
			}
			log.debug "sort studies"
			if(myStudies){
				myStudies.sort{it.shortName}
			}
			def isGdocAdmin = securityService.isUserGDOCAdmin(session.userId)
			session.isGdocAdmin = isGdocAdmin
			log.debug "show $session.userId admin options? $session.isGdocAdmin"
			session.myStudies = myStudies
			def myCollaborationGroups = []
			myCollaborationGroups = securityService.getCollaborationGroups(session.userId)
			def sharedListIds = []
			sharedListIds = userListService.getSharedListIds(session.userId)
			session.sharedListIds = sharedListIds
			//get shared anaylysis and places them in session scope
			def sharedAnalysisIds = []
			sharedAnalysisIds = savedAnalysisService.getSharedAnalysisIds(session.userId)
			session.sharedAnalysisIds = sharedAnalysisIds
			session.dataAvailability = quickStartService.getMyDataAvailability(session.myStudies)
			
			session.myCollaborationGroups = myCollaborationGroups
		
			loadRemoteSources()
			session.profileLoaded = true
			log.debug session.myCollaborationGroups
		}
	}
	
	def cleanup(userId){
		def user = GDOCUser.findByLoginName(userId)
		def myLists = []
		def listsTBD = []
		def analysesTBD = []
		listsTBD = userListService.getTempListIds(userId)
		analysesTBD = savedAnalysisService.getTempAnalysisIds(userId)
		if(listsTBD || analysesTBD){
			cleanupService.cleanupAtLogin(user,listsTBD,analysesTBD)
		}
		
	}
	
	def gatherTempArtifacts(artifactList){
		def artifactsTBD = []
		if(artifactList){
			artifactList.each{ artifact ->
				if(artifact.tags?.contains(Constants.TEMPORARY)){
					log.debug "found $artifact marked as temporary"
					artifactsTBD << artifact.id
				}
			}
		}
		return artifactsTBD
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
