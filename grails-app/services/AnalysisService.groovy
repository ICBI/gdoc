import gov.nih.nci.caintegrator.analysis.messaging.ClassComparisonRequest
import gov.nih.nci.caintegrator.analysis.messaging.ExpressionLookupRequest
import gov.nih.nci.caintegrator.analysis.messaging.SampleGroup
import gov.nih.nci.caintegrator.analysis.messaging.ReporterGroup
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator
import org.springframework.web.context.request.RequestContextHolder as RCH

class AnalysisService {

	def receiveQueue
	def jmsTemplate
	def notificationService
	def idService
	 
	def strategies = [
		(AnalysisType.CLASS_COMPARISON): { sess, cmd ->
			def request = new ClassComparisonRequest(sess, "ClassComparison_" + System.currentTimeMillis())
			request.dataFileName = cmd.dataFile
			def group1 = new SampleGroup()
			def samples = idService.samplesForListName(cmd.groups[0])
			group1.addAll(samples)
			println "group 1: " + samples
			def baseline = new SampleGroup()
			def baselineSamples = idService.samplesForListName(cmd.groups[1])
			println "baseline samples: $baselineSamples"
			baseline.addAll(baselineSamples)
			request.pValueThreshold = cmd.pvalue.toDouble()
			request.foldChangeThreshold = cmd.foldChange.toDouble()
			
			request.group1 = group1
			request.baselineGroup = baseline
			return request
		},
		(AnalysisType.GENE_EXPRESSION): { sess, cmd ->
			def request = new ExpressionLookupRequest(sess, "ExpressionLookup_" + System.currentTimeMillis())
			request.dataFileName = cmd.dataFile
			def sampleGroup = new SampleGroup()
			sampleGroup.addAll(idService.binaryFileIds)
			def reporterGroup = new ReporterGroup()
			reporterGroup.addAll(["1565483_at", "1565484_x_at", "201983_s_at", "201984_s_at", "210984_x_at"])
			request.reporters = reporterGroup
			return request
		}
	
	]
	
	def sendRequest(sessionId, command) {
		def request
		try {
			def userId = RCH.currentRequestAttributes().session.userId
			println "sending message"
			strategies.each { key, value ->
				println "KEY: $key ${command.requestType}"
			}
			request = strategies[command.requestType].call(userId, command)
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
			notificationService.addNotification(userId, item, command.requestType)
			println "after send"
		} catch (Exception e) {
			println "Failed to send request for test" + e
		}
		return request.taskId
	}
}