class NotificationController {

	def savedAnalysisService
	def securityService
	
    def index = { 
		session.notifications = savedAnalysisService.getAllSavedAnalysis(session.userId)
	}
	
	def check = {
		session.notifications = savedAnalysisService.getAllSavedAnalysis(session.userId)
		render(template:"/notification/notificationTable")
	}
	
	def delete = {
		savedAnalysisService.deleteSavedAnalysis(session.userId, params.id)
		session.notifications = savedAnalysisService.getAllSavedAnalysis(session.userId)
		render(template:"/notification/notificationTable")
	}
	
	def share = {
		def notification = savedAnalysisService.getSavedAnalysis(session.userId, params.id)
		securityService.share(notification, "DEVELOPERS")
	}
}
