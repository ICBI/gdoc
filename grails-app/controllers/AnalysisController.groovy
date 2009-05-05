import grails.converters.*
import java.text.*
import java.math.*


class AnalysisController {

	def analysisService
	def notificationService
	def fileBasedAnnotationService
	
    def index = {
		session.study = StudyDataSource.findByShortName("EDINBURGH")
		StudyContext.setStudy("EDIN")
		def lists = UserList.findAll()
		def patientLists = lists.findAll { item ->
			(item.tags.contains("patient") && item.tags.contains(StudyContext.getStudy()))
		}
		println lists[0].tags
		session.lists = patientLists
	}
	
	def submit = { AnalysisCommand cmd ->
		println "Command: " + cmd.groups
		println analysisService
		println "groups : " + cmd.groups
		println "pvalue : " + cmd.pvalue
		println "foldChange : " + cmd.foldChange
		println cmd.errors
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index')
		} else {
			analysisService.sendRequest(session.id, cmd)
			redirect(controller:'notification')
		}

	}
	
	def view = {
		def taskId = params.id
		session.results = notificationService.getNotifications(session.id)[taskId].item
		def columns = []
		def formatOptions = [target: '_blank', baseLinkUrl: 'http://www.genecards.org/cgi-bin/carddisp.pl', showAction: '', addParam: '']
		columns << [index: "reporterId", name: "Reporter ID", sortable: true, width: '100']
		columns << [index: "pvalue", name: "p-value", sortable: true, width: '100']
		columns << [index: "meanGrp1", name: "Group Average", sortable: true, width: '100']
		columns << [index: "foldChange", name: "Fold Change", sortable: true, width: '100']
		columns << [index: "geneSymbol", name: "Gene Symbol", sortable: false, width: '100', formatter: 'genecard', formatoptions: formatOptions]
		def colNames = ["Reporter ID", "p-value", "Group Average", "Fold Change", "Gene Symbol"]
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
			def geneName = fileBasedAnnotationService.annotations[result.reporterId]
			cells << result.reporterId
			def formatter = new DecimalFormat("0.#####E0");
			cells << formatter.format(result.pvalue).replace('E', ' x 10<sup>') + '</sup>'
			cells << result.meanGrp1
			cells << result.foldChange
			cells << geneName
			results << [id: geneName, cell: cells]
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
