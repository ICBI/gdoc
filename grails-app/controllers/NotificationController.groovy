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
		def today = new Date()
		analysis.each{
			def statusMap = [:]
			if(today.minus(it[3]) < 2){
				statusMap["id"] = it[0]
				statusMap["type"] = it[1]
				statusMap["status"] = it[2]
				statusMap["dateCreated"] = it[3]
				statusList << statusMap
			}
		}
		session.notifications = statusList.sort { it.dateCreated }.reverse()
	}
	
	def delete = {
		savedAnalysisService.deleteAnalysis(params.id)
		redirect(action:index)
	}
	
	def error = {
		def notification = SavedAnalysis.get(params.id)
		if(notification) {
			def errorMessage = notification.analysis.item.errorMessage.replace(",", ", ")
			def words = wrapntab(errorMessage)
			render words.join("<br/>")
		}
	}
	
	private def wrapntab(input, linewidth = 50, indent = 0) throws IllegalArgumentException {

		def olines = []
		def oline = " " * indent

		input.split(" ").each() { wrd ->
			if( (oline.size() + wrd.size()) <= linewidth ) {
				oline <<= wrd <<= " "
			}else{
				olines += oline
				oline = wrd + " "
			}
		}
		olines += oline
		return olines
	}
}
