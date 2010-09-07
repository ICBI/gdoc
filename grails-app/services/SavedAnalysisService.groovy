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
	
	def checkSavedAnalysis(userId) {
		def criteria = GDOCUser.createCriteria()
		def savedAnalysis = criteria {
			projections {
				analysis {
					property('id')
					property('type')
					property('status')
					property('dateCreated')
				}
			}
			eq("loginName", userId)
		}
		return savedAnalysis
	}
	
	def addSavedAnalysis(userId, notification, command, tags) {
		def user = GDOCUser.findByLoginName(userId)
		log.debug "GOT NOTIFICATION $notification"
		log.debug notification.item.taskId
		def params = command.properties
		log.debug params
		params.keySet().removeAll( ['errors', 'class', 'metaClass', 'annotationService', 'requestType', 'idService'] as Set )
		log.debug "PARAMS: $params"
		def json = params as JSON
		log.debug "COMMAND $json" 
		def newAnalysis = new SavedAnalysis(type: command.requestType, query: params,  analysis: notification , author:user, status: notification.status, taskId: notification.item.taskId)
		def study = StudyDataSource.findBySchemaName(command.study)
		newAnalysis.addToStudies(study)
		newAnalysis.save(flush:true)
		if(tags){
			tags.each {
				//log.debug "add tag, $it to analysis"
				newAnalysis.addTag(it)
			}
		}
		return newAnalysis
	}
	
	
	
	
	def saveAnalysisResult(userId, result, command, tags){
		def user = GDOCUser.findByLoginName(userId)
		//log.debug ("THE RESULT:")
		//log.debug result
		//log.debug ("THE COMMAND PARAMS:")
		def params = command.properties
		params.keySet().removeAll( ['errors', 'class', 'metaClass', 'requestType', 'annotationService'] as Set )
		log.debug "going to send: " + command.requestType + ", " + params + ", " + result + ", " + user
		def newAnalysis = new SavedAnalysis(type: command.requestType, query: params,  analysisData: result , author:user, status: "Complete")
		def study = StudyDataSource.findBySchemaName(command.study)
		newAnalysis.addToStudies(study)
		newAnalysis.save(flush:true)
		if(newAnalysis.type == AnalysisType.KM_GENE_EXPRESSION){
			tags << "GENE_EXPRESSION"
			if(newAnalysis.query.geAnalysisId.toString() != null){
				log.debug "found the related analysis: " + newAnalysis.query.geAnalysisId + " , is it temporary?"
				def relatedAnalysis = SavedAnalysis.get(newAnalysis.query.geAnalysisId)
				if(relatedAnalysis.tags?.contains(Constants.TEMPORARY)){
					tags << Constants.TEMPORARY
				}
			}
		}
		if(tags){
			tags.each {
				//log.debug "add tag, $it to analysis"
				newAnalysis.addTag(it)
			}
		}
		return newAnalysis
	}
	
	
	
	def updateSavedAnalysis(userId, notification) {
		def runningAnalysis = getSavedAnalysis(userId, notification.item.taskId)
		if(runningAnalysis) {
			log.debug "UPDATING ANALYSIS $notification"
			runningAnalysis.analysis = notification
			runningAnalysis.status = notification.status
			runningAnalysis.taskId = notification.item.taskId
			runningAnalysis.save(flush:true)
				
		} else {
			log.debug "ERROR!  Analysis ${notification.item.taskId} not found"
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
		def analysis = SavedAnalysis.get(analysisId)
		if(analysis) {
			if(analysis.type == AnalysisType.KM_GENE_EXPRESSION){
				if(analysis.query.geAnalysisId.toString() != null){
					log.debug "deleted the related analysis: " + analysis.query.geAnalysisId
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
		if(allAnalysis){
			if(timePeriod == "all"){
				return allAnalysis
			}
			else if(timePeriod == "hideShared"){
				log.debug "hide all shared lists"
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
			
		}else{
			return filteredAnalysis
		}
		
	}
	
	def getPaginatedAnalyses(ids,offset){
		def pagedAnalyses = []
		if(ids){
			def idsString = ids.toString().replace("[","")
			idsString = idsString.replace("]","")
			def query = "from SavedAnalysis as sa where sa.id in ("+idsString+") order by sa.dateCreated desc"
			def al = []
			pagedAnalyses = SavedAnalysis.createCriteria().list(
				max:10,
				offset:offset)
				{
				'in'('id',ids)
				}

			al = SavedAnalysis.findAll(query,[max:10,offset:offset])
			pagedAnalyses.clear()
			pagedAnalyses.addAll(al)
			log.debug "myAnalyses -> $pagedAnalyses as Paged set"
		}
		return pagedAnalyses
	}
	
	def getSavedAnalysis(userId, taskId) {
/*		def user = GDOCUser.findByLoginName(userId, ['fetch':[analysis:'eager']])
		def analysis=[]
		analysis = user.analysis*/
/*		def item =  analysis.find{
			it.analysis.item!=null && it.analysis.item.taskId == taskId
		}*/
		def item = SavedAnalysis.findByTaskId(taskId)
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
	
	def analysisIsTemporary(analysisId){
		def compAnalysis = SavedAnalysis.get(analysisId)
		if(compAnalysis.tags?.contains(Constants.TEMPORARY)){
			return true
		}
		else{
			return false
		}
		return false
	}
}