class NotificationService {
	
	def notifications = [:]
	
	def addNotification(sessionId, notification) {
		if(!notifications[sessionId]) {
			notifications[sessionId] = [:]
		}
		println "adding notification " + notification
		notifications[sessionId][notification.item.taskId] = notification
	}
	
	def getNotifications(sessionId) {
		return notifications[sessionId]
	}
}