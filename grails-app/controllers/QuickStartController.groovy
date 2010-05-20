import grails.converters.*

class QuickStartController {
	def clinicalService
	def quickStartService
	def biospecimenService
	def htDataService
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
						def da = quickStartService.getDataAvailability(study, null)
						println "my study's da $da"
						println "da class "+da.class
						println "dava " + da['dataAvailability']
						println "dava class " + da['dataAvailability'].class
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
		if(params.type){
			println "search by data types"
			def dtResults = [:]
			params.keySet().removeAll( ['action','controller','timestamp'] as Set )
			dtResults = quickStartService.getDataAvailability(session.myStudies, params)
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
			results = quickStartService.getDataAvailability(session.myStudies, null)
			session.dataAvailability = results
			render session.dataAvailability as JSON
		}
	  }else render session.dataAvailability as JSON
	}
	
}
