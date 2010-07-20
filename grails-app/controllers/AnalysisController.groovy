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
	
	def selectDataType = {
		if(!session.files[params.dataType])
			render g.select(optionKey: 'name', optionValue: 'description', noSelection: ['': 'Select Data Type First'], id: 'dataFile', name: "dataFile")
		else
			render g.select(optionKey: 'name', optionValue: 'description', from: session.files[params.dataType], id: 'dataFile', name: "dataFile")
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
		
		def author = GDOCUser.findByLoginName(session.userId)
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
		def analysisResult = savedAnalysisService.getSavedAnalysis(params.id)
		StudyContext.setStudy(analysisResult.query["study"])
		session.results = analysisResult.analysis.item
		session.analysis = analysisResult
		def columns = []
		def formatOptions = [target: '_blank', baseLinkUrl: 'http://www.genecards.org/cgi-bin/carddisp.pl', showAction: '', addParam: '']
		columns << [index: "reporterId", name: "Reporter ID", sortable: true, width: '100']
		columns << [index: "pvalue", name: "p-value", sortable: true, width: '100']
		columns << [index: "meanGrp1", name: "Group Average", sortable: true, width: '100']
		columns << [index: "foldChange", name: "Fold Change", sortable: true, width: '100']
		columns << [index: "geneSymbol", name: "Gene Symbol", sortable: false, width: '100', formatter: 'genecard', formatoptions: formatOptions]
		columns << [index: "target", name: "Target Data?", sortable: true, width: '100']
		def colNames = ["Reporter ID", "p-value", "Group Average", "Fold Change", "Gene Symbol", "Target Data"]
		session.columnJson = columns as JSON
		session.columnNames = colNames as JSON
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
		def results = []
		def sortedEntries = analysisResults.resultEntries.sort { r1, r2 ->
			if(params.sord != 'asc') {
				return r1[sortColumn].compareTo(r2[sortColumn])
			} else {
				return r2[sortColumn].compareTo(r1[sortColumn])
			}
		}
		sortedEntries.getAt(startIndex..<endIndex).each { result ->
			def cells = []
			def geneName = annotationService.findGeneForReporter(result.reporterId)
			def targetData = ""
			if(geneName){
				targetData = drugDiscoveryService.findTargetsFromAlias(geneName)
				if(!targetData)
				targetData = ""
			}
			cells << result.reporterId
			def sciFormatter = new DecimalFormat("0.000E0")
			def formatter = new DecimalFormat("0.000")
			cells << sciFormatter.format(result.pvalue).replace('E', ' x 10<sup>') + '</sup>'
			cells << formatter.format(result.meanGrp1)
			cells << formatter.format(result.foldChange)
			cells << geneName
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
}
