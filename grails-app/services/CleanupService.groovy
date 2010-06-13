class CleanupService implements serviceinterfaces.SessionCleanerServiceInt {
	
	def void cleanup(String userId, HashSet lists, HashSet analyses){
		println "cleanup temporary artifacts for $userId"
		def author = GDOCUser.findByLoginName(userId)
		removeTemporaryAnalyses(analyses, author)
		removeTemporaryLists(lists, author)
	}
	
	
	def removeTemporaryAnalyses(analyses, author){
		/**def analyses = []
		def analysesTBD = []
		analyses = SavedAnalysis.findAllByAuthor(author,[fetch:[tags:"eager"]])
		analyses.each{ analysis ->
			if(analysis.tags?.contains(Constants.TEMPORARY)){
				analysesTBD << analysis
			}
		}**/
		if(analyses){
			def tbSize = analyses.size()
			analyses.each{
				def sa = SavedAnalysis.get(it)
				sa.delete(flush: true)
			}
			println "deleted $tbSize temp analyses for $author.loginName";
		}
	}
	
	def removeTemporaryLists(lists, author){
		/**def lists = []
		def listsTBD = []
		lists = UserList.findAllByAuthor(author,[fetch:[tags:"eager"]])
		lists.each{ list ->
			if(list.tags?.contains(Constants.TEMPORARY)){
				listsTBD << list
			}
		}**/
		if(lists){
			def tbSize = lists.size()
			lists.each{
				def list = UserList.get(it)
				list.delete(flush: true)
			}
			println "deleted $tbSize temp lists for $author.loginName";
		}
		
	}
	
}