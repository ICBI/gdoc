import gov.nih.nci.caintegrator.analysis.messaging.ClassComparisonRequest
import gov.nih.nci.caintegrator.analysis.messaging.SampleGroup
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator

class AnalysisService {

	def receiveQueue
	def jmsTemplate
	def notificationService
	
	def sendRequest(sessionId) {

		try {
			println "sending message"
			def request = new ClassComparisonRequest(sessionId, "ClassComparison_" + System.currentTimeMillis())
			request.dataFileName = "dataMatrixPublic_10JAN06.Rda"
			def group1 = new SampleGroup()
			group1.addAll(["E09137", "E09138", "E09139"])
			def baseline = new SampleGroup()
			baseline.addAll(["E09151", "E09192", "E09212"])
			request.pValueThreshold = 0.1
			request.foldChangeThreshold = 2
			request.group1 = group1
			request.baselineGroup = baseline
			jmsTemplate.send([
				createMessage: { Object[] params ->
					def session = params[0]
					def message = session.createObjectMessage(request)
					message.setJMSReplyTo(receiveQueue)
					return message
				}
			] as MessageCreator)
			def item = ["status": "Running", "item": request]
			notificationService.addNotification(sessionId, item)
			println "after send"
		} catch (Exception e) {
			println "Failed to send request for test" + e
		}
	}
}