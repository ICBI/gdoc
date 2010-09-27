import gov.nih.nci.caintegrator.analysis.messaging.AnalysisResult
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class AnalysisListenerService {
	
	static expose = ['jms']

	static destination = "${CH.config.responseQueue}"
	
	def savedAnalysisService
	def sessionFactory
	
	def onMessage(message) {
		log.debug "GOT MESSAGE: $message"
		def item
		try {
			if(message instanceof AnalysisResult) {
				item = ["status": "Complete", "item" : message]
				savedAnalysisService.updateSavedAnalysis(message.sessionId, item)
			} else {
				def errorItem = message.failedRequest.properties
				errorItem["errorMessage"] = message.message
				item = ["status": "Error", "item" : errorItem]
				savedAnalysisService.updateSavedAnalysis(message.failedRequest.sessionId, item)
			}
		} catch (Exception e) {
			log.error("error saving analysis", e)
			def errorItem = [:]
			errorItem["errorMessage"] = e.getMessage()
			errorItem["taskId"] = message.taskId
			item = ["status": "Error", "item" : errorItem]
			savedAnalysisService.updateSavedAnalysis(message.sessionId, item)
		}
		return null
	}
 
}