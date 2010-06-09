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
		println params	
		def results = []
		
		//OUTCOME based
  		if(params.outcome){
			println "search by outcome"
			def diseases = []
			def studiesToSearch = []
			def outcome = [:]
			
			//find studies for diseases
			if(params.diseases && session.myStudies){
				if(params.diseases.metaClass.respondsTo(params.diseases,"max")){
					diseases = params.diseases as List
					studiesToSearch = session.myStudies.findAll{diseases.contains(it.cancerSite)}
				}else{
					println "one disease "
					diseases << params.diseases
					studiesToSearch = session.myStudies.findAll{ diseases.contains(it.cancerSite)}
				}
			println "studies, " + studiesToSearch.collect{it.shortName} + "  and diseases = $diseases"	
			}
			params.keySet().removeAll( ['action','diseases','controller','timestamp'] as Set )
			outcome["outcomeType"] = SemanticHelper.determineOutcomeLabel(params.outcome)
			results << outcome
		
			//iterate over each study and run run clinical query with 'equivalent' attributes
			studiesToSearch.each{ study ->
				StudyContext.setStudy(study.schemaName)
				session.dataTypes = AttributeType.findAll().sort { it.longName }
				//semantically resolve criteria purposes across studies
				def result = quickStartService.queryOutcomes(params, study, session.dataTypes)
				if(result){
					if(session.dataAvailability){
						session.dataAvailability['dataAvailability'].each{elm ->
							if(elm['STUDY'] == study.shortName){
								println "add breakdown for " + elm['STUDY']
								result["dataBreakdown"] = elm
							}
						}
					}else{
						def da = quickStartService.getMyDataAvailability(study)
						if(da){
							da['dataAvailability'].each{elm ->
								println "retrieve da values for $study.shortName from query"
								result["dataBreakdown"] = elm
							}
						}
					}
					results << result
				}
			}
			println results.flatten() as JSON
			render results as JSON
		}
		
		//DATA BASED
		//code will go here
		if(params.molProfilingType){
			println "search by data types"
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
							println "found $k for " + elm['STUDY']
							dtArray << elm
						}
					}
					
				}
			
			dtResults["dataAvailability"] = dtArray
			println dtResults
			render dtResults as JSON
		}
		
		
		//println results.flatten() as JSON
		render results as JSON
	}
	
	def setupQuickStart = {
		def vocabList = [:]
		def attList = [""]
		if(!session.dataAvailability){
		if(session.myStudies) {
			println "I don not have da in session"
			def results = quickStartService.getDataAvailability(session.myStudies)
			session.dataAvailability = results
			render session.dataAvailability as JSON
		}
	  }else {
		println "I DO have da in session = $session.dataAvailability"
		render session.dataAvailability as JSON
		}
	}
	
	def analysis = {
		println params
		def study = StudyDataSource.findByShortName(params.study)
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
			def existingList = UserList.findByName(params.group1Name)
			if(existingList){
				existingList.delete(flush:true)
				println "deleted " + existingList
			}
			list1 = userListService.createAndReturnList(session.userId,params.group1Name,group1Ids,studies,tags)
			println "created 1st list, $list1"
		}
		if(params.group2Ids){
			group2Ids = ParamsHelper.itemsToList(params.group2Ids)
			def existingList2 = UserList.findByName(params.group2Name)
			if(existingList2){
				existingList2.delete(flush:true)
				println "deleted " + existingList2
			}
			list2 = userListService.createAndReturnList(session.userId,params.group2Name,group2Ids,studies,tags)
			println "created 2nd list, $list2"
		}
		if(list1 && list2){
			println "created both lists, $list1.name, $list2.name successfully"
			redirect(controller:"analysis", action:"index", params:[baselineGroup:list1.name,groups:list2.name]) 
		}
		else{
			redirect(controller:"analysis", action:"index", params:[quickstart:'true']) 
		}
		
		return 
	}
	
	
	def clinical = {
		println params
		def study = StudyDataSource.findByShortName(params.study)
		StudyContext.setStudy(study.schemaName)
		session.study = study
		redirect(controller:"clinical") 
	}
	
}
