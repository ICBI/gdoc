class NotificationController {

	def savedAnalysisService
	def securityService
	def genePatternService
	
    def index = { 
		buildNotifications()
		println session.notifications
	}
	
	def check = {
		buildNotifications()
		render(template:"/notification/notificationTable")
	}
	
	def delete = {
		savedAnalysisService.deleteAnalysis(params.id)
		buildNotifications()
		render(template:"/notification/notificationTable")
	}
	
	def buildNotifications = {
		def notifications = []
		def savedAnalysis = []
		savedAnalysis = savedAnalysisService.getAllSavedAnalysis(session.userId)
		
		def gpJobs = genePatternService.checkJobs(session.userId, session.genePatternJobs)
		if(savedAnalysis)
			notifications.addAll(savedAnalysis)
		if(gpJobs)
			notifications.addAll(gpJobs)
		
		//remove analysis that are more than 2 days old
		def today = new Date()
		def removeable = []
		notifications.each{ n ->
			//println today.minus(n.dateCreated)
			if(today.minus(n.dateCreated) > 2){
				//println "remove " + n
				removeable << n
			}
		}
		notifications.removeAll(removeable)
		
		notifications = notifications.sort { one, two ->
			def dateOne = one.dateCreated
			def dateTwo = two.dateCreated
			return dateTwo.compareTo(dateOne)
		}
		session.notifications = notifications
	}
}
