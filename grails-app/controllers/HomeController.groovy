class HomeController {
	def feedService
	def feedMap
	def summaryService
	
    def index = { 

		if(session.userId) {
			redirect(controller:'studyDataSource')
		}
		session.patientSummary = summaryService.patientSummary()
		session.studySummary = summaryService.studySummary()
		session.sampleSummary = summaryService.sampleSummary()
	}
	
}
