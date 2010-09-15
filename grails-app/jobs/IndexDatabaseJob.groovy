
class IndexDatabaseJob {
    def searchableService

	static triggers = {
	    cron name: 'myIndexTrigger', cronExpression: "0 0 2 * * ? *"
	 }
	
    def execute() {
		Thread.start {
		    def begin = (System.currentTimeMillis()/1000)/60
	        log.info "-------begin forked thread that re-Indexes database------------ at " + System.nanoTime()
		    searchableService.index()
		    def end = (System.currentTimeMillis()/1000)/60
			def elapsed = end - begin
			log.info "-------end re-Indexing database------------ took $elapsed minutes"
		}
    }
}
