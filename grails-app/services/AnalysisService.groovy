import gov.nih.nci.caintegrator.analysis.messaging.ClassComparisonRequest
import gov.nih.nci.caintegrator.analysis.messaging.SampleGroup
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator

class AnalysisService {

	def receiveQueue
	def jmsTemplate
	def notificationService
	def idService
	 
	def strategies = [
		classComparison: { sess, cmd ->
			def request = new ClassComparisonRequest(sess, "ClassComparison_" + System.currentTimeMillis())
			request.dataFileName = cmd.dataFile
			def group1 = new SampleGroup()
			def samples = idService.samplesForListName(cmd.groups[0])
			group1.addAll(["Edinburgh_1063_SEL", "Edinburgh_1004_SEL", "Edinburgh_1023_SEL"])
			println "group 1: " + samples
			def baseline = new SampleGroup()
			def baselineSamples = idService.samplesForListName(cmd.groups[1])
			println "baseline samples: $baselineSamples"
			baseline.addAll(["Edinburgh_1065_SEL", "Edinburgh_1048_SEL", "Edinburgh_1021_SEL"])
			request.pValueThreshold = cmd.pvalue.toDouble()
			request.foldChangeThreshold = cmd.foldChange.toDouble()
			
			request.group1 = group1
			request.baselineGroup = baseline
			return request
		}
	
	]
	
	def sendRequest(sessionId, command) {

		try {
			println "sending message"
			def request = strategies['classComparison'].call(sessionId, command)
			println request
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