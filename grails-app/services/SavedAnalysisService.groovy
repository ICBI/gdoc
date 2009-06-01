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
		def newAnalysis = new SavedAnalysis(type: command.requestType, query: params,  analysis: notification , author:user)
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
		def newAnalysis = new SavedAnalysis(type: command.requestType, query: params,  analysisData: result , author:user)
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
		def notifications = user.analysis
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
	
	def deleteAnalysis(analysisId) {
		println "delete analysis: " + analysisId
		def analysis = SavedAnalysis.get(analysisId)
		if(analysis) {
			analysis.delete(flush: true)
		}
	}
	
	def getSavedAnalysis(userId, taskId) {
		println "TASK ID to look for:  $taskId"
		def user = GDOCUser.findByLoginName(userId)
		println "GOT USER:  $user"
		def analysis=[]
		analysis = user.analysis
		println "ANALYSIS COUNT BEFORE REFRESH:  $analysis"
		user.refresh()
		def analysis2 = user.analysis
		println "ANALYSIS COUNT AFTER REFRESH:  $analysis2"
		def item =  analysis.find{
			it.analysis.item!=null && it.analysis.item.taskId == taskId
		}
		if(item){
			item.refresh()
		}
		println "GOT ITEM= $item"
		return item
	}
	
	def getSavedAnalysis(analysisId) {
		def item = SavedAnalysis.get(analysisId)
		return item
	}
}