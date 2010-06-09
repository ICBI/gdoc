class CleanupService implements serviceinterfaces.SessionCleanerServiceInt {
	
	def void cleanup(String userId){
		println "cleanup temporary artifacts for $userId"
		def author = GDOCUser.findByLoginName(userId)
		removeTemporaryAnalyses(author)
		removeTemporaryLists(author)
	}
	
	
	def removeTemporaryAnalyses(author){
		def analyses = []
		def analysesTBD = []
		analyses = SavedAnalysis.findAllByAuthor(author,[fetch:[tags:"eager"]])
		analyses.each{ analysis ->
			if(analysis.tags?.contains(Constants.TEMPORARY)){
				analysesTBD << analysis
			}
		}
		if(analysesTBD){
			def tbSize = analysesTBD.size()
			analysesTBD.each{
				it.delete(flush: true)
			}
			println "deleted $tbSize temp analyses for $author.loginName";
		}
	}
	
	def removeTemporaryLists(author){
		def lists = []
		def listsTBD = []
		lists = UserList.findAllByAuthor(author,[fetch:[tags:"eager"]])
		lists.each{ list ->
			if(list.tags?.contains(Constants.TEMPORARY)){
				listsTBD << list
			}
		}
		if(listsTBD){
			def tbSize = listsTBD.size()
			listsTBD.each{
				it.delete(flush: true)
			}
			println "deleted $tbSize temp lista for $author.loginName";
		}
		
	}
	
}