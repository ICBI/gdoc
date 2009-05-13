class NotificationController {

	def notificationService
	def securityService
	
    def index = { 
		session.notifications = notificationService.getNotifications(session.userId)
	}
	
	def check = {
		session.notifications = notificationService.getNotifications(session.userId)
		render(template:"/notification/notificationTable")
	}
	
	def delete = {
		notificationService.deleteNotification(session.userId, params.id)
		session.notifications = notificationService.getNotifications(session.userId)
		render(template:"/notification/notificationTable")
	}
	
	def share = {
		def notification = notificationService.getNotification(session.userId, params.id)
		securityService.share(notification, "DEVELOPERS")
	}
}
