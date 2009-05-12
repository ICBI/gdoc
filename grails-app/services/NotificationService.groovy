import grails.converters.*

class NotificationService {
	
	def notifications = [:]
	
	def addNotification(userId, notification, command) {
		def user = GDOCUser.findByUsername(userId)
		println "GOT NOTIFICATION $notification"
		println notification.item.taskId
		def params = command.properties
		params.keySet().removeAll( ['errors', 'class', 'metaClass', 'fileBasedAnnotationService', 'requestType'] as Set )
		println "PARAMS: $params"
		def json = params as JSON
		println "COMMAND $json" 
		def newAnalysis = new SavedAnalysis(type: command.requestType, query: params,  analysis: notification , author:user)
		newAnalysis.save(flush:true)
	}
	
	def updateNotification(userId, notification) {
		def runningAnalysis = getNotification(userId, notification.item.taskId)
		if(runningAnalysis) {
			println "UPDATING ANALYSIS $notification"
			def data = notification as AnalysisJSON
			println "NOTIFICATION JSON $data"
			runningAnalysis.analysis = notification
		} else {
			throw new Exception("No analysis to update")
		}
	}
	
	def getNotifications(userId) {
		def user = GDOCUser.findByUsername(userId)
		def notifications = user.analysis
		notifications = notifications.sort { one, two ->
			def dateOne = new java.sql.Date(one.analysis.item.taskId.substring(one.analysis.item.taskId.indexOf('_') + 1).toLong())
			def dateTwo = new java.sql.Date(two.analysis.item.taskId.substring(two.analysis.item.taskId.indexOf('_') + 1).toLong())
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
		user.refresh()
		def analysis = user.analysis
		def item =  analysis.find {
			it.analysis.item.taskId == taskId
		}
		item.refresh()
		return item
	}
}