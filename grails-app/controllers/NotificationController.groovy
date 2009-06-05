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
		//filter out analysis that are not retrieved asynchronously
		def removeable = []
		/**savedAnalysis.findAll{ s -> 
			if(s.type == AnalysisType.KM_PLOT){
			println "add to removeable " + s
			removeable << s
			}
		}
		savedAnalysis.removeAll(removeable)**/
		
		def gpJobs = genePatternService.checkJobs(session.userId, session.genePatternJobs)
		if(savedAnalysis)
			notifications.addAll(savedAnalysis)
		if(gpJobs)
			notifications.addAll(gpJobs)
		notifications = notifications.sort { one, two ->
			def dateOne = one.dateCreated
			def dateTwo = two.dateCreated
			return dateTwo.compareTo(dateOne)
		}
		session.notifications = notifications
	}
}
