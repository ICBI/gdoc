import grails.converters.*

class SavedAnalysisService {
	
	def securityService
	
	def addSavedAnalysis(userId, notification, command) {
		def user = GDOCUser.findByLoginName(userId)
		println "GOT NOTIFICATION $notification"
		println notification.item.taskId
		def params = command.properties
		params.keySet().removeAll( ['errors', 'class', 'metaClass', 'annotationService', 'requestType'] as Set )
		println "PARAMS: $params"
		def json = params as JSON
		println "COMMAND $json" 
		def newAnalysis = new SavedAnalysis(type: command.requestType, query: params,  analysis: notification , author:user, status: notification.status, taskId: notification.item.taskId)
		newAnalysis.save(flush:true)
	}
	
	def saveAnalysisResult(userId, result, command){
		def user = GDOCUser.findByLoginName(userId)
		//println ("THE RESULT:")
		//println result
		//println ("THE COMMAND PARAMS:")
		def params = command.properties
		params.keySet().removeAll( ['errors', 'class', 'metaClass', 'requestType'] as Set )
		println "going to send: " + command.requestType + ", " + params + ", " + result + ", " + user
		def newAnalysis = new SavedAnalysis(type: command.requestType, query: params,  analysisData: result , author:user, status: "Complete")
		return newAnalysis.save(flush:true)
	}
	
	
	def updateSavedAnalysis(userId, notification) {
		def runningAnalysis = getSavedAnalysis(userId, notification.item.taskId)
		if(runningAnalysis) {
			println "UPDATING ANALYSIS $notification"
			runningAnalysis.analysis = notification
			runningAnalysis.status = notification.status
			runningAnalysis.taskId = notification.item.taskId
		} else {
			throw new Exception("No analysis to update")
		}
	}
	
	def getAllSavedAnalysis(userId) {
		def user = GDOCUser.findByLoginName(userId)
		def groupAnalysisIds = securityService.getSharedItemIds(userId, SavedAnalysis.class.name)
		def notifications = user.analysis
		return notifications
	}
	
	def deleteSavedAnalysis(userId, taskId) {
		def user = GDOCUser.findByLoginName(userId)
		def analysis = user.analysis
		def toDelete = analysis.find {
			it.taskId == taskId
		}
		if(toDelete) {
			user.analysis.remove(toDelete)
			toDelete.delete(flush: true)
		}
	}
	
	def deleteAnalysis(analysisId) {
		println "delete analysis: " + analysisId
		def analysis = SavedAnalysis.get(analysisId)
		if(analysis) {
			analysis.delete(flush: true)
		}
	}
	
	def getSavedAnalysis(userId, taskId) {
		def user = GDOCUser.findByLoginName(userId)
		user.refresh()
		def analysis=[]
		analysis = user.analysis
		def item =  analysis.find{
			it.analysis.item!=null && it.analysis.item.taskId == taskId
		}
		if(item){
			item.refresh()
		}
		return item
	}
	
	def getSavedAnalysis(analysisId) {
		def item = SavedAnalysis.get(analysisId)
		if(item){
			item.refresh()
		}
		return item
	}
}