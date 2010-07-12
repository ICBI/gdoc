import gov.nih.nci.caintegrator.analysis.messaging.AnalysisResult
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class AnalysisListenerService {
	
	static expose = ['jms']

	static destination = "${CH.config.responseQueue}"
	
	def savedAnalysisService
	def sessionFactory
	
	def onMessage(message) {
		println "GOT MESSAGE: $message"
		def item
		if(message instanceof AnalysisResult) {
			item = ["status": "Complete", "item" : message]
			savedAnalysisService.updateSavedAnalysis(message.sessionId, item)
		} else {
			println "ERROR MESSAGE: ${message.failedRequest.properties}"
			def errorItem = message.failedRequest.properties
			errorItem["errorMessage"] = message.message
			println "ERROR item: ${errorItem}"
			item = ["status": "Error", "item" : errorItem]
			savedAnalysisService.updateSavedAnalysis(message.failedRequest.sessionId, item)
		}
		return null
	}
 
}