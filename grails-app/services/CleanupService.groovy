class CleanupService implements serviceinterfaces.SessionCleanerServiceInt {
	def savedAnalysisService
	
	def void cleanup(String userId, HashSet lists, HashSet analyses){
		log.debug "cleanup temporary artifacts for $userId"
		def author = GDOCUser.findByLoginName(userId)
		removeTemporaryAnalyses(analyses, author)
		removeTemporaryLists(lists, author)
	}
	
	def void cleanupAtLogin(GDOCUser user, List lists, List analyses){
		log.debug "at login, cleanup temporary artifacts for $user.loginName"
		removeTemporaryAnalyses(analyses, user)
		removeTemporaryLists(lists, user)
	}
	
	
	def removeTemporaryAnalyses(analyses, author){
		if(analyses){
			log.debug "delete analyses $analyses"
			def tbSize = analyses.size()
			analyses.each{
				def analysis = SavedAnalysis.get(it)
				if(savedAnalysisService.deleteAnalysis(analysis.id))
				log.debug "deleted $tbSize temp analyses for $author.loginName";
			}
			
		}
	}
	
	def removeTemporaryLists(lists, author){
		if(lists){
			def tbSize = lists.size()
			lists.each{
				def list = UserList.get(it)
				if(list)
				list.delete(flush: true)
			}
			log.debug "deleted $tbSize temp lists for $author.loginName";
		}
		
	}
	
}