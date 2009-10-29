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
		def groups = []
		groups << [name: 'Relapse', max: "150", min:"45", mean:"102", minRange:"55", maxRange:"110", patients: [1, 2, 3, 4, 5]]
		groups << [name: 'No Relapse', max:"108", min:"42", mean:"87", minRange:"47", maxRange:"90", patients: [6, 7, 8, 9, 10]]
		analysisResults['groups'] = groups
		render analysisResults as JSON
	}
}
