import grails.converters.*

class QuickStartController {
	def clinicalService
	def quickStartService
	def biospecimenService
	def htDataService
	def userListService
    def index = { 
		
	}
	
	def quickSearch = {
		log.debug params	
		def results = []
		
		//OUTCOME based
  		if(params.outcome){
			log.debug "search by outcome"
			def diseases = []
			def studiesToSearch = []
			def outcome = [:]
			
			//find studies for diseases
			if(params.diseases && session.myStudies){
				if(params.diseases.metaClass.respondsTo(params.diseases,"max")){
					diseases = params.diseases as List
					studiesToSearch = session.myStudies.findAll{diseases.contains(it.cancerSite)}
				}else{
					log.debug "one disease "
					diseases << params.diseases
					studiesToSearch = session.myStudies.findAll{ diseases.contains(it.cancerSite)}
				}
			log.debug "studies, " + studiesToSearch.collect{it.shortName} + "  and diseases = $diseases"	
			}
			params.keySet().removeAll( ['action','diseases','controller','timestamp'] as Set )
			outcome["outcomeType"] = SemanticHelper.determineOutcomeLabel(params.outcome)
			def queryOutcome = SemanticHelper.determineOutcomeQuery(params.outcome)
			def da
			if(session.dataAvailability){
				da = session.dataAvailability['dataAvailability']
			}else{
				def allda = quickStartService.getMyDataAvailability(studiesToSearch)
				if(allda){
					da = allda['dataAvailability']
				}
			}
			results = quickStartService.queryOutcomes(queryOutcome,studiesToSearch,da)
			results << outcome
			
			/**REFACTORED to use canned queries above
			iterate over each study and run run clinical query with 'equivalent' attributes
			studiesToSearch.each{ study ->
				StudyContext.setStudy(study.schemaName)
				session.dataTypes = AttributeType.findAll().sort { it.longName }
				//semantically resolve criteria purposes across studies
				def result = quickStartService.queryOutcomes(params, study, session.dataTypes)
				println "result: $result" 
				if(result){
					if(session.dataAvailability){
						session.dataAvailability['dataAvailability'].each{elm ->
							if(elm['STUDY'] == study.shortName){
								log.debug "add breakdown for " + elm['STUDY']
								result["dataBreakdown"] = elm
							}
						}
					}else{
						def da = quickStartService.getMyDataAvailability(study)
						if(da){
							da['dataAvailability'].each{elm ->
								log.debug "retrieve da values for $study.shortName from query"
								result["dataBreakdown"] = elm
							}
						}
					}
					results << result
				}
			}**/
			log.debug results.flatten() as JSON
			render results as JSON
		}
		
		//DATA BASED
		//code will go here
		if(params.molProfilingType){
			log.debug "search by data types"
			def dtResults = [:]
			def existingDtResults = [:]
			def dtArray = []
			params.keySet().removeAll( ['action','controller','timestamp'] as Set )
			if(session.dataAvailability){
				existingDtResults = session.dataAvailability
			}
			else{
				existingDtResults = quickStartService.getMyDataAvailability(session.myStudies)
			}
				existingDtResults['dataAvailability'].each{elm ->
					elm.each{ k,v ->
						if((params.molProfilingType == k) && (v > 0)){
							log.debug "found $k for " + elm['STUDY']
							dtArray << elm
						}
					}
					
				}
			
			dtResults["dataAvailability"] = dtArray
			log.debug dtResults["dataAvailability"]
			render dtResults as JSON
		}
		
		
		//log.debug results.flatten() as JSON
		render results as JSON
	}
	
	def setupQuickStart = {
		def vocabList = [:]
		def attList = [""]
		if(!session.dataAvailability){
		if(session.myStudies) {
			log.debug "I don not have da in session"
			def results = quickStartService.getDataAvailability(session.myStudies)
			session.dataAvailability = results
			render session.dataAvailability as JSON
		}
	  }else {
		log.debug "I DO have da in session = $session.dataAvailability"
		render session.dataAvailability as JSON
		}
	}
	
	def analysis = {
		log.debug params
		def study = StudyDataSource.findByShortName(params.study)
		def author = GDOCUser.findByUsername(session.userId)
		session.study = study
		StudyContext.setStudy(study.schemaName)
		def studies = []
		def group1Ids = []
		def group2Ids = []
		def tags = []
		def list1
		def list2
		studies << study.schemaName
		if(params.tags){
			tags = ParamsHelper.itemsToList(params.tags)
			tags << Constants.TEMPORARY
		}
		if(params.group1Ids){
			group1Ids = ParamsHelper.itemsToList(params.group1Ids)
			def existingList = UserList.findByAuthorAndName(author,params.group1Name)
			if(existingList){
				list1 = existingList
				log.debug "retain existing 1st list, $list1.name by same name"
			}else{
				list1 = userListService.createAndReturnList(session.userId,params.group1Name,group1Ids,studies,tags)
				log.debug "created 1st list, $list1"
			}
			
		}
		if(params.group2Ids){
			group2Ids = ParamsHelper.itemsToList(params.group2Ids)
			def existingList2 = UserList.findByAuthorAndName(author,params.group2Name)
			if(existingList2){
				list2 = existingList2
				log.debug "retain existing 2nd list, $list2.name by same name"
			}else{
				list2 = userListService.createAndReturnList(session.userId,params.group2Name,group2Ids,studies,tags)
				log.debug "created 2nd list, $list2"
			}
			
		}
		if(list1 && list2){
			session.tempLists << list1.id
			session.tempLists << list2.id
			log.debug "created or retained both lists, $list1.name, $list2.name successfully, and added ids to session: $session.tempLists"
			if(params.type == 'group'){
				log.debug "do group comparison analysis from qs"
				redirect(controller:"analysis", action:"index", params:[baselineGroup:list1.name,groups:list2.name]) 
			}	
			else if(params.type == 'km'){
				log.debug "do km plot from quickstart"
				redirect(controller:"km", action:"index", params:[baselineGroup:list1.name,groups:list2.name])
			}
			else if(params.type == 'pca'){
				log.debug "do pca plot from quickstart"
				redirect(controller:"pca", action:"index", params:[baselineGroup:list1.name,groups:list2.name])
			}
			else if(params.type == 'heatMap'){
				log.debug "do heatmp plot from quickstart"
				redirect(controller:"heatMap", action:"index", params:[baselineGroup:list1.name,groups:list2.name])
			}
			else if(params.type == 'cin'){
				log.debug "do cin from quickstart"
				redirect(controller:"cin", action:"index", params:[baselineGroup:list1.name,groups:list2.name])
			}
			
		}
		else{
			if(params.type == 'group'){
				log.debug "do group comparison analysis from qs"
				redirect(controller:"analysis", action:"index", params:[baselineGroup:list1.name,groups:list2.name]) 
			}	
			else if(params.type == 'km'){
				log.debug "do km plot from quickstart"
				redirect(controller:"km", action:"index", params:[baselineGroup:list1.name,groups:list2.name])
			}
			else if(params.type == 'pca'){
				log.debug "do pca plot from quickstart"
				redirect(controller:"pca", action:"index", params:[baselineGroup:list1.name,grouppca:list2.name])
			}
			else if(params.type == 'heatMap'){
				log.debug "do heatmp plot from quickstart"
				redirect(controller:"heatMap", action:"index", params:[baselineGroup:list1.name,groups:list2.name])
			}
			else if(params.type == 'cin'){
				log.debug "do cin from quickstart"
				redirect(controller:"cin", action:"index", params:[baselineGroup:list1.name,groups:list2.name])
			}
		}
		
		return 
	}
	
	
	def clinical = {
		log.debug params
		def study = StudyDataSource.findByShortName(params.study)
		StudyContext.setStudy(study.schemaName)
		session.study = study
		redirect(controller:"clinical") 
	}
	
}
