class NotificationController {

	def notificationService
	
    def index = { 
		session.notifications = notificationService.getNotifications(session.id)
		println session.notifications
	}
}
