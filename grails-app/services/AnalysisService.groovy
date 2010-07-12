import gov.nih.nci.caintegrator.analysis.messaging.ClassComparisonRequest
import gov.nih.nci.caintegrator.analysis.messaging.ExpressionLookupRequest
import gov.nih.nci.caintegrator.analysis.messaging.PrincipalComponentAnalysisRequest
import gov.nih.nci.caintegrator.analysis.messaging.HeatMapRequest
import gov.nih.nci.caintegrator.analysis.messaging.SampleGroup
import gov.nih.nci.caintegrator.analysis.messaging.ReporterGroup
import gov.nih.nci.caintegrator.enumeration.*
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessageCreator
import org.springframework.web.context.request.RequestContextHolder as RCH

class AnalysisService {

	def receiveQueue
	def jmsTemplate
	def savedAnalysisService
	def annotationService
	def idService
	 
	def strategies = [
		(AnalysisType.CLASS_COMPARISON): { sess, cmd ->
			def request = new ClassComparisonRequest(sess, "ClassComparison_" + System.currentTimeMillis())
			request.dataFileName = cmd.dataFile
			def group1 = new SampleGroup()
			def samples = idService.samplesForListName(cmd.groups)
			def allIds = idService.sampleIdsForFile(cmd.dataFile)
			samples = allIds.intersect(samples)
			group1.addAll(samples)
			println "group 1: " + samples
			def baseline = new SampleGroup()
			println "my baselineGroup is $cmd.baselineGroup"
			def baselineSamples = idService.samplesForListName(cmd.baselineGroup)
			//def baselineSamples = idService.samplesForListName(cmd.groups[1])
			baselineSamples = allIds.intersect(baselineSamples)
			println "baseline samples: $baselineSamples"
			baseline.addAll(baselineSamples)
			request.pValueThreshold = cmd.pvalue.toDouble()
			request.foldChangeThreshold = cmd.foldChange.toDouble()
			
			request.multiGrpComparisonAdjType = MultiGroupComparisonAdjustmentType.valueOf(cmd.adjustment)
			request.statisticalMethod = StatisticalMethodType.valueOf(cmd.statisticalMethod)
			request.group1 = group1
			request.baselineGroup = baseline
			return request
		},
		(AnalysisType.GENE_EXPRESSION): { sess, cmd ->
			def request = new ExpressionLookupRequest(sess, "ExpressionLookup_" + System.currentTimeMillis())
			request.dataFileName = cmd.dataFile
			def sampleGroup = new SampleGroup()
			sampleGroup.addAll(idService.sampleIdsForFile(cmd.dataFile))
			def reporterGroup = new ReporterGroup()
			reporterGroup.addAll(annotationService.findReportersForGene(cmd.geneName))
			request.reporters = reporterGroup
			request.samples = sampleGroup
			return request
		},
		(AnalysisType.KM_GENE_EXPRESSION): { sess, cmd ->
			def request = new ExpressionLookupRequest(sess, "ExpressionLookup_" + System.currentTimeMillis())
			request.dataFileName = cmd.dataFile
			def sampleGroup = new SampleGroup()
			def allIds = idService.sampleIdsForFile(cmd.dataFile)
			if(!cmd.groups == 'ALL') {
				def sampleIds = idService.samplesForListName(cmd.groups)
				println "SAMPLEIDS: $sampleIds"
				allIds = allIds.intersect(sampleIds)
				println "ALLIDS: $allIds"
			}
			sampleGroup.addAll(allIds)
			def reporterGroup = new ReporterGroup()
			reporterGroup.addAll(annotationService.findReportersForGene(cmd.geneName))
			request.reporters = reporterGroup
			request.samples = sampleGroup
			return request
		},
		(AnalysisType.PCA): { sess, cmd ->
			def request = new PrincipalComponentAnalysisRequest(sess, "PCA_" + System.currentTimeMillis())
			request.dataFileName = cmd.dataFile
			def group1 = new SampleGroup()
			def allIds = idService.sampleIdsForFile(cmd.dataFile)
			group1.addAll(allIds)
			println "group 1: " + allIds
/*			if(cmd.reporterCriteria == 'variance' && cmd.variance)
				request.varianceFilterValue = cmd.variance.toDouble() / 100.0
			else if(cmd.reporterCriteria == 'foldChange' && cmd.foldChange)
				request.foldChangeFilterValue = cmd.foldChange.toDouble()*/
			request.sampleGroup = group1
			if(cmd.reporterList) {
				def reporterGroup = new ReporterGroup()
				reporterGroup.addAll(idService.reportersForListName(cmd.reporterList))
				println "REPORTERS: $reporterGroup"
				request.reporterGroup = reporterGroup
			}
			return request
		},
		(AnalysisType.HEATMAP): { sess, cmd ->
			def request = new HeatMapRequest(sess, "HEATMAP_" + System.currentTimeMillis())
			request.dataFileName = cmd.dataFile
			def group1 = new SampleGroup()
			def allIds = idService.sampleIdsForFile(request.dataFileName)
			if(!cmd.patientList == 'ALL') {
				def sampleIds = idService.samplesForListName(cmd.patientList)
				println "SAMPLEIDS: $sampleIds"
				allIds = allIds.intersect(sampleIds)
				println "ALLIDS: $allIds"
			}
			group1.addAll(allIds)
			println "group 1: " + allIds
			request.sampleGroup = group1
			if(cmd.geneList) {
				def reporterGroup = new ReporterGroup()
				reporterGroup.addAll(annotationService.findReportersForGeneList(cmd.geneList))
				println "REPORTERS: $reporterGroup"
				request.reporterGroup = reporterGroup
			} else if(cmd.reporterList) {
				def reporterGroup = new ReporterGroup()
				reporterGroup.addAll(idService.reportersForListName(cmd.reporterList))
				println "REPORTERS: $reporterGroup"
				request.reporterGroup = reporterGroup
			}
			return request
		}
	]
	
	def sendRequest(sessionId, command, tags) {
		def request
		println "in send request"
		try {
			def userId = RCH.currentRequestAttributes().session.userId
			println "sending message"
			strategies.each { key, value ->
				println "KEY: $key ${command.requestType}"
			}
			request = strategies[command.requestType].call(userId, command)
			println request
			def item = ["status": "Running", "item": request]
			def newAnalysis = savedAnalysisService.addSavedAnalysis(userId, item, command, tags)
			jmsTemplate.send([
				createMessage: { Object[] params ->
					def session = params[0]
					def message = session.createObjectMessage(request)
					message.setJMSReplyTo(receiveQueue)
					return message
				}
			] as MessageCreator)
			println "after send"
		} catch (Exception e) {
			println "Failed to send request for test " + e
		}
		return request.taskId
	}
	
}