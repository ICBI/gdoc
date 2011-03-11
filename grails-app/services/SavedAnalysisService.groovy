import grails.converters.*

class SavedAnalysisService {
	
	def securityService
	
	def getAllSavedAnalysis(userId,sharedIds){
		def user = GDOCUser.findByUsername(userId)
		user.refresh()
		def analyses = user.analysis
		def analysisIds = []
		def sharedAnalysisIds = []
		sharedAnalysisIds = sharedIds
		
		if(!sharedAnalysisIds){
			sharedAnalysisIds = getSharedAnalysisIds(userId,false)
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
			eq("username", userId)
		}
		return savedAnalysis
	}
	
	def addSavedAnalysis(userId, notification, command, tags) {
		def user = GDOCUser.findByUsername(userId)
		log.debug notification.item.taskId
		def params = command.properties
		log.debug "PARAMS: " + params
		params.keySet().removeAll( ['errors', 'class', 'metaClass', 'annotationService', 'requestType', 'idService', 'userListService', 'constraints'] as Set )
		def json = params as JSON
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
		def user = GDOCUser.findByUsername(userId)
		//log.debug ("THE RESULT:")
		//log.debug result
		//log.debug ("THE COMMAND PARAMS:")
		def params = command.properties
		params.keySet().removeAll( ['errors', 'class', 'metaClass', 'requestType', 'annotationService', 'userListService', 'constraints'] as Set )
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
			runningAnalysis.analysis = notification
			runningAnalysis.status = notification.status
			runningAnalysis.taskId = notification.item.taskId
			runningAnalysis.save(flush:true)
				
		} else {
			log.debug "ERROR!  Analysis ${notification.item.taskId} not found"
		}
	}
	
	def getAllSavedAnalysis(userId) {
		def user = GDOCUser.findByUsername(userId)
		//def groupAnalysisIds = securityService.getSharedItemIds(userId, SavedAnalysis.class.name,false)
		def notifications = user.analysis
		return notifications
	}
	
	def deleteAnalysis(analysisId) {
		def analysis = SavedAnalysis.get(analysisId)
		if(analysis) {
			if(analysis.type == AnalysisType.KM_GENE_EXPRESSION){
				if(analysis.query.geAnalysisId.toString() != 'null'){
					log.debug "deleting the related analysis: " + analysis.query.geAnalysisId
					def id = new Long(analysis.query.geAnalysisId)
					def relatedAnalysis = SavedAnalysis.get(id)
					if(relatedAnalysis)
						relatedAnalysis.delete(flush: true)
				}
				analysis.delete(flush: true)
			}else{
				analysis.delete(flush: true)
			}
		}
	}
	
	def getFilteredAnalysis(timePeriod, userId, sharedIds){
		
		def filteredAnalysis = []
		
			if(timePeriod == "all"){
				log.debug "show ALL user's analyses"
				filteredAnalysis = getAllSavedAnalysis(userId, sharedIds)
				return filteredAnalysis
			}
			else if(timePeriod == "hideShared"){
				log.debug "only show user's analyses"
				def user = GDOCUser.findByUsername(userId)
				filteredAnalysis = user.analysis
				return filteredAnalysis
			}
			else{
				def tp = Integer.parseInt(timePeriod)
				def today = new Date()
				def allAnalysis = []
				def user = GDOCUser.findByUsername(userId)
				allAnalysis = user.analysis
				allAnalysis.each{ analysis ->
					if(today.minus(analysis.dateCreated) <= tp){
						filteredAnalysis << analysis
					}
				}
				return filteredAnalysis
			}
			
		return filteredAnalysis
		
	}
	
	def getPaginatedAnalyses(filter,sharedIds,offset,userId){
		def pagedAnalyses = []
		def user = GDOCUser.findByUsername(userId)
		if(filter == "all"){
			pagedAnalyses = getAllAnalyses(sharedIds,offset,user)
		}else if(filter == "hideShared"){
			pagedAnalyses = getUsersAnalyses(offset,user)
		}else{
			pagedAnalyses = getUsersAnalysesByTimePeriod(filter,offset,user)
		}
		return pagedAnalyses
	}
		
	
	def getAllAnalyses(sharedIds,offset,user){
		def pagedAnalyses = []
		if(sharedIds){
			def ids =[]
			sharedIds.each{
				ids << new Long(it)
			}
			pagedAnalyses = SavedAnalysis.createCriteria().list(
				max:10,
				offset:offset)
				{
					and{
						'ne'('status', "Running")
						'order'("dateCreated", "desc")
					}
					or {
						eq("author", user)
						'in'('id',ids)
					}
				}
			log.debug "all analysis -> $pagedAnalyses as Paged set"
		}else{
			pagedAnalyses = SavedAnalysis.createCriteria().list(
				max:10,
				offset:offset)
				{
					and{
						'ne'('status', "Running")
						'order'("dateCreated", "desc")
						'eq'("author", user)
					}
				}
		}
		return pagedAnalyses
	}
	
	def getUsersAnalyses(offset,user) {
		def userAnalysis = [:]
		def pagedAnalyses = [:]
		def total
		pagedAnalyses = SavedAnalysis.createCriteria().list(
			max:10,
			offset:offset)
			{
				and{
					'ne'('status', "Running")
					'order'("dateCreated", "desc")
					'eq'("author", user)
				}
			}
		log.debug "user analysis only-> $pagedAnalyses as Paged set"
		return pagedAnalyses
	}
	
	def getUsersAnalysesByTimePeriod(timePeriod,offset,user) {
		def pagedAnalyses = []
		def now = new Date()
		def tp = Integer.parseInt(timePeriod)
		pagedAnalyses = SavedAnalysis.createCriteria().list(
			max:10,
			offset:offset)
			{
				and{
					'ne'('status', "Running")
					'between'('dateCreated',now-tp,now)
					'order'("dateCreated", "desc")
				}
				or {
					eq("author", user)
				}
			}
		log.debug "user analysis only over past $timePeriod days-> $pagedAnalyses as Paged set"
		return pagedAnalyses
	}
	
	def getAllAnalysesIds(sharedIds,userId){
		def savedAnalysisIds = []
		def analysisHQL
		if(sharedIds){
			def ids =[]
			sharedIds.each{
				ids << new Long(it)
			}
			analysisHQL = "SELECT distinct analysis.id FROM SavedAnalysis analysis JOIN analysis.author author " + 
			"WHERE author.username = :username OR analysis.id IN (:ids) "
			savedAnalysisIds = UserList.executeQuery(analysisHQL,[username:userId, ids:ids])
		}else{
			analysisHQL = "SELECT distinct analysis.id FROM SavedAnalysis analysis JOIN analysis.author author " + 
			"WHERE author.username = :username "
			savedAnalysisIds = SavedAnalysis.executeQuery(analysisHQL,[username:userId])
		}
		log.debug "got savedAnalysis ids $savedAnalysisIds"
		return savedAnalysisIds
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
					if(analysis.author.username == userId){
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
	
	
	
	def getSavedAnalysis(userId, taskId) {
		def item = SavedAnalysis.findByTaskId(taskId)
		if(item){
			item.refresh()
			item.studies
		}
		return item
	}
	
	def getTempAnalysisIds(userId) {
		
		String findByTagHQL = """
		   SELECT DISTINCT analysis.id
		   FROM SavedAnalysis analysis, TagLink tagLink
		   JOIN analysis.author author
		   WHERE analysis.id = tagLink.tagRef
		   AND author.username = :username
		   AND tagLink.type = 'savedAnalysis'
		   AND tagLink.tag.name = :tag
		"""
		
		def savedAnalysisIds = SavedAnalysis.executeQuery(findByTagHQL, [tag: Constants.TEMPORARY, username: userId])
		return savedAnalysisIds	
	}
	
	def getSharedAnalysisIds(userId,refresh){
		def sharedAnalysisIds = []
		sharedAnalysisIds = securityService.getSharedItemIds(userId, SavedAnalysis.class.name,refresh)
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