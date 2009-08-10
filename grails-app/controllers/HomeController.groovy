class HomeController {
	def feedService
	def feedMap
	def summaryService
	
    def index = { 
	 	feedMap = feedService.getFeed()
		if(feedMap!=null){
			print feedMap.size()
		}
		if(session.userId) {
			redirect(controller:'studyDataSource')
		}
		session.patientSummary = summaryService.patientSummary()
		session.studySummary = summaryService.studySummary()
	}
	
}
