class NotificationController {

	def notificationService
	
    def index = { 
		session.notifications = notificationService.getNotifications(session.userId)
		println session.notifications
	}
	
	def check = {
		session.notifications = notificationService.getNotifications(session.userId)
		render(template:"/notification/notificationTable")
	}
}
