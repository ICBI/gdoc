import grails.converters.*

class RandomForestController {

	def analysisService
	def savedAnalysisService
	def idService
	def userListService
	
    def index = {
		session.study = StudyDataSource.get(params.id)
		StudyContext.setStudy(session.study.schemaName)
		def lists = userListService.getAllLists(session.userId,session.sharedListIds)
		def patientLists = lists.findAll { item ->
			(item.tags.contains("patient") && item.schemaNames().contains(StudyContext.getStudy()))
		}
		session.patientLists = patientLists.sort { it.name }	
	}
	
	def submit = {
		redirect(action:'view')
	}
	
	def view = {
		
	}
	
	def results = {
		def analysisResults = [:]
		def items = []
		def item1 = [:]
		def item2 = [:]
		def groups = []
		groups << [name: 'Relapse', max: "150", min:"45", mean:"102", minRange:"55", maxRange:"110", patients: [1, 2, 3, 4, 5]]
		groups << [name: 'No Relapse', max:"108", min:"42", mean:"87", minRange:"47", maxRange:"90", patients: [6, 7, 8, 9, 10]]
		item1["id"] = "MZ_RT1"
		item1["foldChange"] = 2.0
		item1["groups"] = groups
		items << item1
		groups = []
		groups << [name: 'Relapse', max: "200", min:"30", mean:"150", minRange:"55", maxRange:"190", patients: [1, 2, 3, 4, 5]]
		groups << [name: 'No Relapse', max:"208", min:"20", mean:"87", minRange:"47", maxRange:"200", patients: [6, 7, 8, 9, 10]]
		item2["id"] = "MZ_RT2"
		item2["foldChange"] = 1.5
		item2["groups"] = groups
		items << item2
		
		analysisResults['items'] = items
		render analysisResults as JSON
	}
}
