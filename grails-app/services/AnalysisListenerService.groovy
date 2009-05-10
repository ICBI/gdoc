import gov.nih.nci.caintegrator.analysis.messaging.AnalysisResult
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH


class AnalysisListenerService {
	
	static expose = ['jms']

	static destination = "${CH.config.responseQueue}"
	
	def notificationService
	
	def onMessage(message) {
		
		println "GOT MESSAGE: $message"
		def item
		if(message instanceof AnalysisResult) {
			item = ["status": "Complete", "item" : message]
			notificationService.updateNotification(message.sessionId, item)
		} else {
			item = ["status": "Error", "item" : message.failedRequest]
			notificationService.updateNotification(message.failedRequest.sessionId, item)
		}
		
		
	}
 
}