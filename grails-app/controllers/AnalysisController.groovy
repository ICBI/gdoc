class AnalysisController {

	def analysisService
	
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
}
