import grails.converters.*

class NotificationService {
	
	def notifications = [:]
	
	def addNotification(userId, notification) {
		def user = GDOCUser.findByUsername(userId)
		def runningAnalysis = getNotification(userId, notification.item.taskId)
		if(runningAnalysis) {
			runningAnalysis.analysis = notification
		} else {
		
			def newAnalysis = new SavedAnalysis(type:AnalysisType.CLASS_COMPARISON, analysis: notification , author:user)
			newAnalysis.save()
		}
	}
	
	def getNotifications(userId) {
		def user = GDOCUser.findByUsername(userId)
		def notifications = user.analysis.collect { 
			it.analysis 
		} 
		notifications = notifications.sort { one, two ->
			def dateOne = new java.sql.Date(one.item.taskId.substring(one.item.taskId.indexOf('_') + 1).toLong())
			def dateTwo = new java.sql.Date(two.item.taskId.substring(two.item.taskId.indexOf('_') + 1).toLong())
			return dateTwo.compareTo(dateOne)
		}
		return notifications
	}
	
	def deleteNotification(userId, taskId) {
		def user = GDOCUser.findByUsername(userId)
		def analysis = user.analysis
		def toDelete = analysis.find {
			it.analysis.item.taskId == taskId
		}
		if(toDelete) {
			user.analysis.remove(toDelete)
			toDelete.delete(flush: true)
		}
	}
	
	def getNotification(userId, taskId) {
		def user = GDOCUser.findByUsername(userId)
		def analysis = user.analysis
		return analysis.find {
			it.analysis.item.taskId == taskId
		}
	}
}