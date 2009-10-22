import grails.converters.*

class SavedAnalysisService {
	
	def securityService
	
	def addSavedAnalysis(userId, notification, command) {
		def user = GDOCUser.findByLoginName(userId)
		println "GOT NOTIFICATION $notification"
		println notification.item.taskId
		def params = command.properties
		println params
		params.keySet().removeAll( ['errors', 'class', 'metaClass', 'annotationService', 'requestType'] as Set )
		println "PARAMS: $params"
		def json = params as JSON
		println "COMMAND $json" 
		def newAnalysis = new SavedAnalysis(type: command.requestType, query: params,  analysis: notification , author:user, status: notification.status, taskId: notification.item.taskId)
		if(newAnalysis.save(flush:true)){
			newAnalysis.addTag(command.study);
		}
	}
	
	def saveAnalysisResult(userId, result, command){
		def user = GDOCUser.findByLoginName(userId)
		//println ("THE RESULT:")
		//println result
		//println ("THE COMMAND PARAMS:")
		def params = command.properties
		params.keySet().removeAll( ['errors', 'class', 'metaClass', 'requestType', 'annotationService'] as Set )
		println "going to send: " + command.requestType + ", " + params + ", " + result + ", " + user
		def newAnalysis = new SavedAnalysis(type: command.requestType, query: params,  analysisData: result , author:user, status: "Complete")
		if(newAnalysis.save(flush:true)){
			println "saved analysis, time to add tags"
			newAnalysis.addTag(command.study);
			return newAnalysis
		}
	}
	
	
	def updateSavedAnalysis(userId, notification) {
		def runningAnalysis = getSavedAnalysis(userId, notification.item.taskId)
		if(runningAnalysis) {
			println "UPDATING ANALYSIS $notification"
			runningAnalysis.analysis = notification
			runningAnalysis.status = notification.status
			runningAnalysis.taskId = notification.item.taskId
			println "ANALYSIS: $runningAnalysis"
			runningAnalysis.save(flush:true)
		} else {
			throw new Exception("No analysis to update")
		}
	}
	
	def getAllSavedAnalysis(userId) {
		def user = GDOCUser.findByLoginName(userId)
		//def groupAnalysisIds = securityService.getSharedItemIds(userId, SavedAnalysis.class.name)
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
			if(analysis.type == AnalysisType.KM_GENE_EXPRESSION){
				if(analysis.query.geAnalysisId.toString() != null){
					println "deleted the related analysis: " + analysis.query.geAnalysisId
					def relatedAnalysis = SavedAnalysis.get(analysis.query.geAnalysisId)
					relatedAnalysis.delete(flush: true)
				}
				analysis.delete(flush: true)
			}else{
				analysis.delete(flush: true)
			}
		}
	}
	
	def filterAnalysis(timePeriod, allAnalysis){
		if(timePeriod == "all"){
			return allAnalysis
		}
		else{
		def tp = Integer.parseInt(timePeriod)
		def filteredAnalysis = []
		def today = new Date()
		allAnalysis.each{ analysis ->
			if(today.minus(analysis.dateCreated) <= tp){
				filteredAnalysis << analysis
			}
		}
		return filteredAnalysis
		}
	}
	
	def getSavedAnalysis(userId, taskId) {
		def user = GDOCUser.findByLoginName(userId, ['fetch':[analysis:'eager']])
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
	
	def getSharedAnalysisIds(userId){
		def sharedAnalysisIds = []
		sharedAnalysisIds = securityService.getSharedItemIds(userId, SavedAnalysis.class.name)
		return sharedAnalysisIds
	}
	
	def getSavedAnalysis(analysisId) {
		def item = SavedAnalysis.get(analysisId)
		if(item){
			item.refresh()
		}
		return item
	}
}