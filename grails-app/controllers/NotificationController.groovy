import grails.converters.*

class NotificationController {

	def savedAnalysisService
	def securityService
	def genePatternService
	def invitationService
	
    def index = { 
		checkNotifications()
	}
	
	def check = {
		checkNotifications()
		render(template:"/notification/notificationTable")
	}
	
	def checkNotifications = {
		def analysis = savedAnalysisService.checkSavedAnalysis(session.userId)
		def statusList = []
		analysis.each{
			def statusMap = [:]
			statusMap["id"] = it[0]
			statusMap["type"] = it[1]
			statusMap["status"] = it[2]
			statusMap["dateCreated"] = it[3]
			statusList << statusMap
		}
		session.notifications = statusList.sort { it.dateCreated }.reverse()
	}
	
	def delete = {
		savedAnalysisService.deleteAnalysis(params.id)
		buildNotifications()
		render(template:"/notification/notificationTable")
	}
	
	def buildNotifications = {
		def notifications = []
		def savedAnalysis = []
		def invitations = []
		savedAnalysis = savedAnalysisService.getAllSavedAnalysis(session.userId)
		if(savedAnalysis){
			notifications.addAll(savedAnalysis)
		}
		
		//remove analysis that are more than 2 days old
		def today = new Date()
		def removeable = []
		notifications.each{ n ->
			//log.debug today.minus(n.dateCreated)
			if(today.minus(n.dateCreated) > 2){
				//log.debug "remove " + n
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
