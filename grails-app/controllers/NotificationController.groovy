class NotificationController {

	def savedAnalysisService
	def securityService
	def genePatternService
	def invitationService
	
    def index = { 
		buildNotifications()
		println session.notifications
		println session.invitations
	}
	
	def check = {
		buildNotifications()
		render(template:"/notification/notificationTable")
	}
	
	def delete = {
		savedAnalysisService.deleteAnalysis(params.id)
		buildNotifications()
		render(template:"/notification/notificationTable")
	}
	
	def buildNotifications = {
		def notifications = []
		def savedAnalysis = []
		def invitations = []
		savedAnalysis = savedAnalysisService.getAllSavedAnalysis(session.userId)
		invitations = invitationService.findAllInvitationsForUser(session.userId)
		session.invitations = invitations
		
		def gpJobs = genePatternService.checkJobs(session.userId, session.genePatternJobs)
		if(savedAnalysis){
			notifications.addAll(savedAnalysis)
			savedAnalysis.each{
				if(savedAnalysisService.analysisIsTemporary(it.id)){
					//if(!session.tempAnalyses?.contains(it.id)){
						session.tempAnalyses << it.id
					//}
				}
			}
		}
		if(gpJobs)
			notifications.addAll(gpJobs)
		
		//remove analysis that are more than 2 days old
		def today = new Date()
		def removeable = []
		notifications.each{ n ->
			//println today.minus(n.dateCreated)
			if(today.minus(n.dateCreated) > 2){
				//println "remove " + n
				removeable << n
			}
		}
		notifications.removeAll(removeable)
		
		notifications = notifications.sort { one, two ->
			def dateOne = one.dateCreated
			def dateTwo = two.dateCreated
			return dateTwo.compareTo(dateOne)
		}
		session.notifications = notifications
	}
}
