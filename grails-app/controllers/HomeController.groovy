class HomeController {
	def feedService
	def feedMap
	def summaryService
	
    def index = { 

		session.patientSummary = summaryService.patientSummary()
		session.studySummary = summaryService.studySummary()
		def sampleSummary = summaryService.sampleSummary()
		if(sampleSummary instanceof Map)
			session.sampleSummary = sampleSummary
	}
	
}
