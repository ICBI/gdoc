class NotificationController {

	def savedAnalysisService
	def securityService
	def genePatternService
	
    def index = { 
		buildNotifications()
		println session.notifications
	}
	
	def check = {
		buildNotifications()
		render(template:"/notification/notificationTable")
	}
	
	def delete = {
		savedAnalysisService.deleteSavedAnalysis(session.userId, params.id)
		buildNotifications()
		render(template:"/notification/notificationTable")
	}
	
	def share = {
		def notification = savedAnalysisService.getSavedAnalysis(session.userId, params.id)
		securityService.share(notification, "DEVELOPERS")
	}
	
	def buildNotifications = {
		def notifications = []
		notifications.addAll(savedAnalysisService.getAllSavedAnalysis(session.userId))
		notifications.addAll(genePatternService.checkJobs(session.userId, session.genePatternJobs))
		notifications = notifications.sort { one, two ->
			def dateOne = one.dateCreated
			def dateTwo = two.dateCreated
			return dateTwo.compareTo(dateOne)
		}
		session.notifications = notifications
	}
}
