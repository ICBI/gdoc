import grails.converters.*

class SavedAnalysisService {
	
	def securityService
	
	def getAllSavedAnalysis(userId,sharedIds){
		def user = GDOCUser.findByLoginName(userId)
		user.refresh()
		def analyses = user.analysis
		def analysisIds = []
		def sharedAnalysisIds = []
		sharedAnalysisIds = sharedIds
		
		if(!sharedAnalysisIds){
			sharedAnalysisIds = getSharedAnalysisIds(userId)
		}
		if(analyses){
			if(analyses.metaClass.respondsTo(analyses, "size")) {
				analyses.each{
					analysisIds << it.id.toString()
				}
			} else {
				analysisIds << analyses.id.toString()
			}
		}
			
		def sharedAnalyses = []
		//until we modify ui, just add shared lists to 'all' lists
		sharedAnalysisIds.each{
			if(!(analysisIds.contains(it))){
				def foundAnalysis = SavedAnalysis.get(it.toString())
				if(foundAnalysis){
					analyses << foundAnalysis
				}
		   	}
		}
		analyses = analyses.sort { one, two ->
			def dateOne = one.dateCreated
			def dateTwo = two.dateCreated
			return dateTwo.compareTo(dateOne)
		}
		
		
		return analyses
	}
	
	def addSavedAnalysis(userId, notification, command) {
		def user = GDOCUser.findByLoginName(userId)
		println "GOT NOTIFICATION $notification"
		println notification.item.taskId
		def params = command.properties
		println params
		params.keySet().removeAll( ['errors', 'class', 'metaClass', 'annotationService', 'requestType'] as Set )
		println "PARAMS: $params"
		def json = params as JSON
		println "COMMAND $json" 
		def newAnalysis = new SavedAnalysis(type: command.requestType, query: params,  analysis: notification , author:user, status: notification.status, taskId: notification.item.taskId)
		def study = StudyDataSource.findBySchemaName(command.study)
		newAnalysis.addToStudies(study)
		newAnalysis.save(flush:true)
	}
	
	def saveAnalysisResult(userId, result, command){
		def user = GDOCUser.findByLoginName(userId)
		//println ("THE RESULT:")
		//println result
		//println ("THE COMMAND PARAMS:")
		def params = command.properties
		params.keySet().removeAll( ['errors', 'class', 'metaClass', 'requestType', 'annotationService'] as Set )
		println "going to send: " + command.requestType + ", " + params + ", " + result + ", " + user
		def newAnalysis = new SavedAnalysis(type: command.requestType, query: params,  analysisData: result , author:user, status: "Complete")
		def study = StudyDataSource.findBySchemaName(command.study)
		newAnalysis.addToStudies(study)
		newAnalysis.save(flush:true)
		return newAnalysis
	}
	
	
	def updateSavedAnalysis(userId, notification) {
		def runningAnalysis = getSavedAnalysis(userId, notification.item.taskId)
		if(runningAnalysis) {
			println "UPDATING ANALYSIS $notification"
			runningAnalysis.analysis = notification
			runningAnalysis.status = notification.status
			runningAnalysis.taskId = notification.item.taskId
			println "ANALYSIS: $runningAnalysis"
			runningAnalysis.save(flush:true)
		} else {
			println "ERROR!  Analysis ${notification.item.taskId} not found"
		}
	}
	
	def getAllSavedAnalysis(userId) {
		def user = GDOCUser.findByLoginName(userId)
		//def groupAnalysisIds = securityService.getSharedItemIds(userId, SavedAnalysis.class.name)
		def notifications = user.analysis
		return notifications
	}
	
	def deleteSavedAnalysis(userId, taskId) {
		def user = GDOCUser.findByLoginName(userId)
		def analysis = user.analysis
		def toDelete = analysis.find {
			it.taskId == taskId
		}
		if(toDelete) {
			user.analysis.remove(toDelete)
			toDelete.delete(flush: true)
		}
	}
	
	def deleteAnalysis(analysisId) {
		println "delete analysis: " + analysisId
		def analysis = SavedAnalysis.get(analysisId)
		if(analysis) {
			if(analysis.type == AnalysisType.KM_GENE_EXPRESSION){
				if(analysis.query.geAnalysisId.toString() != null){
					println "deleted the related analysis: " + analysis.query.geAnalysisId
					def relatedAnalysis = SavedAnalysis.get(analysis.query.geAnalysisId)
					relatedAnalysis.delete(flush: true)
				}
				analysis.delete(flush: true)
			}else{
				analysis.delete(flush: true)
			}
		}
	}
	
	def filterAnalysis(timePeriod, allAnalysis, userId){
		def filteredAnalysis = []
		if(timePeriod == "all"){
			return allAnalysis
		}
		else if(timePeriod == "hideShared"){
			println "hide all shared lists"
			allAnalysis.each{ analysis ->
				if(analysis.author.loginName == userId){
					filteredAnalysis << analysis
				}
			}
			return filteredAnalysis
		}
		else{
			def tp = Integer.parseInt(timePeriod)
			def today = new Date()
			allAnalysis.each{ analysis ->
				if(today.minus(analysis.dateCreated) <= tp){
					filteredAnalysis << analysis
				}
			}
			return filteredAnalysis
		}
	}
	
	def getPaginatedAnalyses(ids,offset){
		def idsString = ids.toString().replace("[","")
		idsString = idsString.replace("]","")
		def query = "from SavedAnalysis as sa where sa.id in ("+idsString+") order by sa.dateCreated desc"
		def al = []
		def pagedAnalyses =
		SavedAnalysis.createCriteria().list(
			max:10,
			offset:offset)
			{
			'in'('id',ids)
			}
		
		al = SavedAnalysis.findAll(query,[max:10,offset:offset])
		pagedAnalyses.clear()
		pagedAnalyses.addAll(al)
		println "myAnalyses -> $pagedAnalyses as Paged set"
		return pagedAnalyses
	}
	
	def getSavedAnalysis(userId, taskId) {
		def user = GDOCUser.findByLoginName(userId, ['fetch':[analysis:'eager']])
		def analysis=[]
		analysis = user.analysis
		def item =  analysis.find{
			it.analysis.item!=null && it.analysis.item.taskId == taskId
		}
		if(item){
			item.refresh()
			item.studies
		}
		return item
	}
	
	def getSharedAnalysisIds(userId){
		def sharedAnalysisIds = []
		sharedAnalysisIds = securityService.getSharedItemIds(userId, SavedAnalysis.class.name)
		return sharedAnalysisIds
	}
	
	def getSavedAnalysis(analysisId) {
		def item = SavedAnalysis.get(analysisId)
		if(item){
			item.refresh()
		}
		return item
	}
}