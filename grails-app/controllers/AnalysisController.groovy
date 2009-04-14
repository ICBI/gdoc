import grails.converters.*

class AnalysisController {

	def analysisService
	def notificationService
	
    def index = {
		def lists = UserList.findAll()
		def patientLists = lists.findAll { item ->
			item.tags.contains("patient")
		}
		println lists[0].tags
		session.lists = patientLists
	}
	
	def submit = {
		println analysisService
		println "groups : " + params.groups
		println "pvalue : " + params.pValue
		analysisService.sendRequest(session.id)
		redirect(controller:'notification')
	}
	
	def view = {
		def taskId = params.id
		session.results = notificationService.getNotifications(session.id)[taskId].item
		def columns = []
		columns << [index: "reporterId", name: "Reporter ID", sortable: true, width: '100']
		columns << [index: "foldChange", name: "Fold Change", sortable: true, width: '100']
		columns << [index: "pvalue", name: "p-value", sortable: true, width: '70']
		def colNames = ["Reporter ID", "Fold Change", "p-value"]
		session.columnJson = columns as JSON
		session.columnNames = colNames as JSON
		println session.columnJson
	}
}
