class NotificationController {

	def notificationService
	
    def index = { 
		session.notifications = notificationService.getNotifications(session.id)
		println session.notifications
	}
	
	def check = {
		session.notifications = notificationService.getNotifications(session.id)
		render(template:"/notification/notificationTable")
	}
}
