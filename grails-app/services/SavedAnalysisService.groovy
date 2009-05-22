import grails.converters.*

class SavedAnalysisService {
	
	def securityService
	
	def addSavedAnalysis(userId, notification, command) {
		def user = GDOCUser.findByLoginName(userId)
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
	
	def updateSavedAnalysis(userId, notification) {
		def runningAnalysis = getSavedAnalysis(userId, notification.item.taskId)
		if(runningAnalysis) {
			println "UPDATING ANALYSIS $notification"
			def data = notification as AnalysisJSON
			println "NOTIFICATION JSON $data"
			runningAnalysis.analysis = notification
		} else {
			throw new Exception("No analysis to update")
		}
	}
	
	def getAllSavedAnalysis(userId) {
		def user = GDOCUser.findByLoginName(userId)
		def groupAnalysisIds = securityService.getSharedItemIds(userId, SavedAnalysis.class.name)
		//def groupAnalysis = SavedAnalysis.getAll(groupAnalysisIds)
		def notifications = user.analysis
		//println "GROUP ANALYSIS: $groupAnalysis"
		//notifications.addAll(groupAnalysis)
		notifications = notifications.sort { one, two ->
			def dateOne = new java.sql.Date(one.analysis.item.taskId.substring(one.analysis.item.taskId.indexOf('_') + 1).toLong())
			def dateTwo = new java.sql.Date(two.analysis.item.taskId.substring(two.analysis.item.taskId.indexOf('_') + 1).toLong())
			return dateTwo.compareTo(dateOne)
		}
		
		return notifications
	}
	
	def deleteSavedAnalysis(userId, taskId) {
		def user = GDOCUser.findByLoginName(userId)
		def analysis = user.analysis
		def toDelete = analysis.find {
			it.analysis.item.taskId == taskId
		}
		if(toDelete) {
			user.analysis.remove(toDelete)
			toDelete.delete(flush: true)
		}
	}
	
	def getSavedAnalysis(userId, taskId) {
		def user = GDOCUser.findByLoginName(userId)
		user.refresh()
		def analysis = user.analysis
		def item =  analysis.find {
			it.analysis.item.taskId == taskId
		}
		item.refresh()
		return item
	}
}