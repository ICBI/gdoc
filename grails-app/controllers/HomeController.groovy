class HomeController {
	def feedService
	def feedMap
	def summaryService
	
    def index = { 
		//get LCCC feed
		feedMap = feedService.getFeed()
		
		session.patientSummary = summaryService.patientSummary()
		session.studySummary = summaryService.studySummary()
		
		//get patient counts for each study
		session.studyCounts = summaryService.patientCounts()
		
		//get anatomic sources
		def sampleSummary = summaryService.sampleSummary()
		if(sampleSummary instanceof Map){
			session.sampleSummary = sampleSummary
			session.anatomicSourceValues = summaryService.anatomicSources(sampleSummary)
		}
	}
	
}
