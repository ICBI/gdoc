import gov.nih.nci.caintegrator.analysis.messaging.AnalysisResult

class AnalysisListenerService {
	
	static expose = ['jms']

	static destination = "AnalysisResponse"
	
	def notificationService
	
	def onMessage(message) {
		
		println "GOT MESSAGE: $message"
		def item
		if(message instanceof AnalysisResult) {
			item = ["status": "Complete", "item" : message]
			notificationService.addNotification(message.sessionId, item)
		} else {
			item = ["status": "Error", "item" : message.failedRequest]
			notificationService.addNotification(message.failedRequest.sessionId, item)
		}
		
		
	}
 
}