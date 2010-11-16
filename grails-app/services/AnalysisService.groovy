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
			log.debug "group 1: " + samples
			def baseline = new SampleGroup()
			log.debug "my baselineGroup is $cmd.baselineGroup"
			def baselineSamples = idService.samplesForListName(cmd.baselineGroup)
			//def baselineSamples = idService.samplesForListName(cmd.groups[1])
			baselineSamples = allIds.intersect(baselineSamples)
			log.debug "baseline samples: $baselineSamples"
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
			reporterGroup.addAll(cmd.reporters)
			request.reporters = reporterGroup
			request.samples = sampleGroup
			return request
		},
		(AnalysisType.KM_GENE_EXPRESSION): { sess, cmd ->
			def request = new ExpressionLookupRequest(sess, "ExpressionLookup_" + System.currentTimeMillis())
			request.dataFileName = cmd.dataFile
			def sampleGroup = new SampleGroup()
			def allIds = idService.sampleIdsForFile(cmd.dataFile)
			log.debug "group sent in = $cmd.groups"
			if(cmd.groups != 'ALL') {
				def sampleIds = idService.samplesForListName(cmd.groups)
				log.debug "SAMPLEIDS: $sampleIds"
				allIds = allIds.intersect(sampleIds)
				log.debug "ALLIDS: $allIds"
			}
			sampleGroup.addAll(allIds)
			def reporterGroup = new ReporterGroup()
			reporterGroup.addAll(annotationService.findReportersForGeneAndFile(cmd.geneName, cmd.dataFile))
			request.reporters = reporterGroup
			request.samples = sampleGroup
			return request
		},
		(AnalysisType.PCA): { sess, cmd ->
			def request = new PrincipalComponentAnalysisRequest(sess, "PCA_" + System.currentTimeMillis())
			request.dataFileName = cmd.dataFile
			def group1 = new SampleGroup()
			if(cmd.patientCriteria == 'ALL') {
				def allIds = idService.sampleIdsForFile(cmd.dataFile)
				group1.addAll(allIds)
			} else {
				def ids = []
				cmd.groups.each {
					ids.addAll(idService.samplesForListName(it))
				}
				group1.addAll(ids)
			}
			request.sampleGroup = group1
			if(cmd.reporterList) {
				def reporterGroup = new ReporterGroup()
				reporterGroup.addAll(idService.reportersForListName(cmd.reporterList))
				log.debug "REPORTERS: $reporterGroup"
				request.reporterGroup = reporterGroup
			}
			return request
		},
		(AnalysisType.HEATMAP): { sess, cmd ->
			def request = new HeatMapRequest(sess, "HEATMAP_" + System.currentTimeMillis())
			log.debug "COMMAND: ${cmd}"
			log.debug "reporterIds: ${cmd.reporterIds}"
			log.debug "from comparison: ${cmd.fromComparison}"
			log.debug "datafile: ${cmd.dataFile}"
			log.debug "request: ${request == null}"
			log.debug "idService: $idService"
			request.dataFileName = cmd.dataFile
			def group1 = new SampleGroup()
			def allIds = idService.sampleIdsForFile(request.dataFileName)
			log.debug "ALLIDS: $allIds"
			if(!cmd.fromComparison && !cmd.patientList == 'ALL') {
				def sampleIds = idService.samplesForListName(cmd.patientList)
				allIds = allIds.intersect(sampleIds)
			}
			
			group1.addAll(allIds)
			log.debug "group 1: " + allIds
			request.sampleGroup = group1
			if(cmd.geneList) {
				def reporterGroup = new ReporterGroup()
				reporterGroup.addAll(annotationService.findReportersForGeneList(cmd.geneList))
				log.debug "REPORTERS: $reporterGroup"
				request.reporterGroup = reporterGroup
			} else if(cmd.reporterList) {
				def reporterGroup = new ReporterGroup()
				reporterGroup.addAll(idService.reportersForListName(cmd.reporterList))
				log.debug "REPORTERS: $reporterGroup"
				request.reporterGroup = reporterGroup
			}
			if(cmd.fromComparison) {
				def reporterGroup = new ReporterGroup()
				log.debug "cmd reporters ${cmd.reporterIds}"
				reporterGroup.addAll(cmd.reporterIds.replace("[", "").replace("]", "").split(",").toList())
				request.reporterGroup = reporterGroup
				log.debug "REPORTERS: $reporterGroup"
			}
			return request
		}
	]
	
	def sendRequest(sessionId, command, tags) {
		def request
		log.debug "in send request"
		try {
			def userId = RCH.currentRequestAttributes().session.userId
			log.debug "sending message: $userId"
			strategies.each { key, value ->
				log.debug "KEY: $key ${command.requestType}"
			}
			request = strategies[command.requestType].call(userId, command)
			log.debug request
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
			log.debug "after send"
		} catch (Exception e) {
			log.error "Failed to send request for test " + e
		}
		return request.taskId
	}
	
}