class WorkflowsController {
	def securityService
	def savedAnalysisService
	def userListService
	def middlewareService
	def quickStartService
	def cleanupService
	def invitationService
	
    def index = { 
		def thisUser = GDOCUser.findByUsername(session.userId)
		//last login
		Date lastLogin = thisUser.lastLogin
		if(!session.profileLoaded){
			def studyNames = securityService.getSharedItemIds(session.userId, StudyDataSource.class.name,false)
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
			
			securityService.setLastLogin(session.userId)
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
			sharedListIds = userListService.getSharedListIds(session.userId,true)
			session.sharedListIds = sharedListIds
			
			if(lastLogin){
				def formattedDate = lastLogin.format('EEE MMM d, yyyy')
				log.debug "users last login was $formattedDate"
				flash.message =  "Welcome back, your last login was $formattedDate. You can check if you have been granted access to new <a href='/gdoc/userList?listFilter=all'>lists</a> or <a href='/gdoc/savedAnalysis?analysisFilter=all'>analyses</a> since your last login"
				//def count = userListService.newListsAvailable(sharedListIds,lastLogin,session.userId)
				//if(count > 0){
				//	log.debug "user has new lists available"
				//	session.listFilter = "all"
				//	flash.message = "You have new <a href='/gdoc/userList'>lists</a> available"
				//}
				
			}
			if(params.firstLogin){
				log.debug "this is the user's first login"
				flash.message = "Welcome ... your account has been created in G-DOC! " +  
				"Your current permissions allow you to view public data sets. Once logged in you may gain access to " +
				"other data sets requesting " + 
				"access to the study group via the 'Collaboration Groups' page."
			}
			
			
			//get shared anaylysis and places them in session scope
			def sharedAnalysisIds = []
			sharedAnalysisIds = savedAnalysisService.getSharedAnalysisIds(session.userId,true)
			session.sharedAnalysisIds = sharedAnalysisIds
			session.dataAvailability = quickStartService.getMyDataAvailability(session.myStudies)

			
			session.myCollaborationGroups = myCollaborationGroups
			loadRemoteSources()
			session.profileLoaded = true
			log.debug session.myCollaborationGroups
			
		}
		def pendingInvites = invitationService.getInvitesThatRequireAction(session.userId,lastLogin)
		if(params.desiredPage){
			log.debug "done with profile loading, user has requested another view"
			redirect(controller:params.desiredPage)
		}
		println "User has " + session.myCollaborationGroups.size() + " collab groups, " + session.sharedListIds.size() + " lists " + session.sharedAnalysisIds.size() + " analyses and " + pendingInvites + " invites"
		[inviteMessage:pendingInvites["inviteMessage"],requestMessage:pendingInvites["requestMessage"]]
	}
	
	
	def cleanup(userId){
		def user = GDOCUser.findByUsername(userId)
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
