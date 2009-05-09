import grails.converters.*

class NotificationService {
	
	def notifications = [:]
	
	def addNotification(userId, notification) {
		def user = GDOCUser.findByUsername(userId)
		def analysis = user.analysis
		def runningAnalysis = analysis.find {
			it.analysis.item.taskId == notification.item.taskId
		}
		if(runningAnalysis) {
			runningAnalysis.analysis = notification
		} else {
		
		def newAnalysis = new SavedAnalysis(type:AnalysisType.CLASS_COMPARISON, analysis: notification , author:user)
		newAnalysis.save()
		}
	}
	
	def getNotifications(userId) {
		println userId
		def user = GDOCUser.findByUsername(userId)
		println "${user.analysis}"
		def notifications = user.analysis.collect { 
			println "${it.analysis}"
			it.analysis 
		}
		return notifications
	}
}