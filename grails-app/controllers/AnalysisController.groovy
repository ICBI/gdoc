import grails.converters.*
import java.text.*
import java.math.*

@Mixin(ControllerMixin)
class AnalysisController {

	def analysisService
	def savedAnalysisService
	def annotationService
	def userListService
	def drugDiscoveryService
	def htDataService
	
    def index = {
	    if(session.study){
			if(params.baselineGroup && params.groups){
				AnalysisCommand cmd  = new AnalysisCommand();
				cmd.baselineGroup = params.baselineGroup 
				cmd.groups = params.groups
				flash.cmd = cmd
				flash.message = " Your 2 lists, $cmd.baselineGroup and $cmd.groups have been prepopulated below for group comparison"	
			}
			StudyContext.setStudy(session.study.schemaName)

			session.files = htDataService.getHTDataMap()
			session.dataSetType = session.files.keySet()
			log.debug "my ht files for $session.study = $session.files"
		}

		def methods = ['TTest':'T-Test: Two Sample Test', 'Wilcoxin':'Wilcoxin Test: Mann-Whitney Test']
		session.statisticalMethods = methods
		def adjustments = ['NONE':'None', 'FWER':'Family-Wise Error Rate(FWER): Bonferroni', 'FDR':'False Discovery Rate(FDR): Benjamini-Hochberg']
		session.adjustments = adjustments

		loadPatientLists()
		[diseases:getDiseases(),myStudies:session.myStudies, params:params]
	}
	
	def submit = { AnalysisCommand cmd ->
		log.debug "analysis params : $params"
		log.debug "Command: " + cmd.groups
		log.debug "type : " + cmd.requestType
		log.debug analysisService
		log.debug "baseline group : " + cmd.baselineGroup
		log.debug "groups : " + cmd.groups
		log.debug "pvalue : " + cmd.pvalue
		log.debug "foldChange : " + cmd.foldChange
		log.debug "study:" + cmd.study 
		log.debug cmd.errors
		def datasetType = params.dataSetType.replace(" ","_")
		def tags = []
		tags << "$datasetType"
		
		def author = GDOCUser.findByUsername(session.userId)
		def list1IsTemp = userListService.listIsTemporary(cmd.groups,author)
		def list2IsTemp = userListService.listIsTemporary(cmd.baselineGroup,author)
		if(list1IsTemp || list2IsTemp){
			tags << Constants.TEMPORARY
		}
		
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			def study = StudyDataSource.findBySchemaName(cmd.study)
			redirect(action:'index',id:study.id)
		} else {
			analysisService.sendRequest(session.id, cmd, tags)
			redirect(controller:'notification')
		}

	}
	
	def view = {
		if(isAccessible(params.id)){
			log.debug "user can access analysis $params.id"
			def analysisResult = savedAnalysisService.getSavedAnalysis(params.id)
			if (analysisResult.type != AnalysisType.CLASS_COMPARISON) {
				log.debug "user CANNOT access analysis $params.id because is not a CLASS_COMPARISON analysis but a $analysisResult.type analysis"
				redirect(controller:'policies', action:'deniedAccess')
			}
			StudyContext.setStudy(analysisResult.query["study"])
			loadCurrentStudy()
			def endpoints = KmAttribute.findAll()
			def jsonEndpoints = endpoints.collect {
				return [it.attributeDescription, it.attribute]
			}
			session.endpoints = jsonEndpoints as JSON
			session.results = analysisResult.analysis.item
			session.analysis = analysisResult
			def columns = []
			def formatOptions = [baseLinkUrl: 'geneLink']
			columns << [index: "reporterId", name: "Reporter ID", sortable: true, width: '100']
			columns << [index: "geneName", name: "Gene Symbol", sortable: true, width: '100', formatter: 'showlink', formatoptions: formatOptions]
			columns << [index: "pvalue", name: "p-value", sortable: true, width: '100']
			columns << [index: "foldChange", name: "Fold Change", sortable: true, width: '100']
			columns << [index: "meanBaselineGrp", name: "Mean Baseline", sortable: true, width: '100']
			columns << [index: "meanGrp1", name: "Mean Group", sortable: true, width: '100']
			columns << [index: "stdBaselineGrp", name: "Std Baseline", sortable: true, width: '100']
			columns << [index: "stdGrp1", name: "Std Group", sortable: true, width: '100']
			columns << [index: "target", name: "Target Data", sortable: false, width: '100']
			def annotation = "Gene Symbol"
			if(session.analysis.tags.contains(DataType.MICRORNA.tag())) {
				annotation = 'microRNA'
			} else if (session.analysis.tags.contains(DataType.COPY_NUMBER.tag())) {
				if (session.results?.resultEntries[0]?.reporterId =~ /^(\d+)$/)
					annotation = "Chromosome"
				else
					annotation = "Cytoband"
			} 
			def colNames = ["Reporter ID", annotation, "p-value", "Fold Change", "Mean Baseline", "Mean Group", "Std Baseline", "Std Group", "Target Data"]
			session.columnJson = columns as JSON
			session.columnNames = colNames as JSON
			session.resultTable = null
		}
		else{
			log.debug "user CANNOT access analysis $params.id"
			redirect(controller:'policies', action:'deniedAccess')
		}
		
	}
	
	def results = {
		def rows = params.rows.toInteger()
		def currPage = params.page.toInteger()
		def startIndex = ((currPage - 1) * rows)
		def endIndex = (currPage * rows)
		def sortColumn = params.sidx
		def analysisResults = session.results
		if(endIndex > analysisResults.resultEntries.size()) {
			endIndex = analysisResults.resultEntries.size()
		}
		
		
		def tempResults = []
		if(!session.resultTable) {
			def reporterIds = analysisResults.resultEntries.collect {
				it.reporterId
			}
			def genes = annotationService.findAllGenesForReporters(reporterIds, session.analysis.query.dataFile)
			analysisResults.resultEntries.each { item ->
				def tempItem = [:]
				def geneName = genes[item.reporterId]
				tempItem.reporterId = item.reporterId
				if(!geneName) 
					tempItem.geneName = ""
				else
					tempItem.geneName = geneName
				tempItem.pvalue = item.pvalue
				tempItem.foldChange = item.foldChange
				tempItem.meanBaselineGrp = item.meanBaselineGrp
				tempItem.meanGrp1 = item.meanGrp1
				tempItem.stdBaselineGrp = item.stdBaselineGrp
				tempItem.stdGrp1 = item.stdGrp1
			
				tempResults << tempItem
			}
			session.resultTable = tempResults
		}
		def sortedResults = session.resultTable.sort { r1, r2 ->
			if(params.sord != 'asc') {
				if(!r1[sortColumn])
					return -1
				if(!r2[sortColumn])
					return 1
				else 
					return r1[sortColumn].compareTo(r2[sortColumn])
			} else {
				if(!r1[sortColumn])
					return 1
				if(!r2[sortColumn])
					return -1
				else
					return r2[sortColumn].compareTo(r1[sortColumn])
			}
		}
		
		def results = []
		
		sortedResults.getAt(startIndex..<endIndex).each { result ->
			def cells = []
			cells << result.reporterId
			cells << result.geneName
			def sciFormatter = new DecimalFormat("0.000E0")
			def formatter = new DecimalFormat("0.000")
			cells << sciFormatter.format(result.pvalue).replace('E', ' x 10<sup>') + '</sup>'
			cells << formatter.format(result.foldChange)
			cells << formatter.format(result.meanBaselineGrp)
			cells << formatter.format(result.meanGrp1)
			cells << formatter.format(result.stdBaselineGrp)
			cells << formatter.format(result.stdGrp1)
			
			// lookup target data
			def targetData = ""
			if(result.geneName){
				targetData = drugDiscoveryService.findTargetsFromAlias(result.geneName)
				if(!targetData)
				targetData = ""
			}
			def targetLinks = []
			if(!targetData.equals("")){
				targetData.each{ target ->
					def link = "<a href='/gdoc/moleculeTarget/show/"+target.id+"'>"+target+"</a>"
					targetLinks << link
				}
			}
			if(targetLinks)
				cells << targetLinks.toString()
			else cells << targetData
			results << [id: result.reporterId, cell: cells]
		}

		def jsonObject = [
			page: currPage,
			total: Math.ceil(analysisResults.resultEntries.size() / rows),
			records: analysisResults.resultEntries.size(),
			rows:results
		]
		render jsonObject as JSON
	}
	
	def download = {
		response.setHeader("Content-disposition", "attachment; filename=data_export.csv")
		response.contentType = "application/octet-stream" 

		def outs = response.outputStream 
		def cols = [:] 
		def columns = JSON.parse(session.columnJson.toString())
		def columnData = columns.collect {
			it.index
		}
		outs << JSON.parse(session.columnNames.toString()).join(",") + "\n"
		session.resultTable.each { data ->
			def temp = []
			columnData.each {
				if(!data[it])
					temp << ''
				else
					temp << data[it]
			}
			outs << temp.join(",")
		    outs << "\n"
		}
		outs.flush()
		outs.close()
	}
}
