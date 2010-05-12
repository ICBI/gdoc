import grails.converters.*

class QuickStartController {
	def clinicalService
	def quickStartService
	def biospecimenService
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
					results << result
				}
			}
		}
		
		//DATA BASED
		//code will go here
		
		println results.flatten() as JSON
		render results as JSON
	}
	
	def setupQuickStart = {
		def vocabList = [:]
		def attList = [""]
		def results = []
		if(session.myStudies) {
			def diseases = []
			diseases = session.myStudies.collect{it.cancerSite}
			diseases.remove("N/A")
			vocabList["diseases"] = diseases as Set
			session.myStudies.each{ study ->
				//println "gather atts for $study"
				StudyContext.setStudy(study.schemaName)
				if(study.content){
					def dataTypes = AttributeType.findAll().sort { it.longName }
					//println dataTypes.collect {it.shortName}
					dataTypes.each{
						if(it.vocabulary){
							if(vocabList[it.shortName]){
								def curVals = []
								curVals = vocabList[it.shortName]
								//println "current Values for $it.shortName : $curVals"
								def v = []
								v = it.vocabs.collect{it.term}
								curVals.addAll(v)
								//println "newest Values for $it.shortName : $curVals"
								def newSet = curVals as Set
								//println "final Values for $it.shortName : $newSet"
								vocabList[it.shortName] = newSet
							}else{
								def v = []
								v = it.vocabs.collect{it.term}
								vocabList[it.shortName] = v
							}
						}
					}
				}
			}
			//println vocabList as JSON
			render vocabList as JSON
		}
	}
	
}
