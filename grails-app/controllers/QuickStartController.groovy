import grails.converters.*

class QuickStartController {
	def clinicalService
	def biospecimenService
    def index = { 
		
	}
	
	def quickSearch = {
		println params
		def diseases = []
		def studiesToSearch = []
		def results = []

		if(params.diseases && session.myStudies){
			if(params.diseases.metaClass.respondsTo(params.diseases,"max")){
				diseases = params.diseases as List
				studiesToSearch = session.myStudies.findAll{ 
					diseases.contains(it.cancerSite)
				}
			}else{
				println "one disease "
				diseases << params.diseases
				studiesToSearch = session.myStudies.findAll{ 
					diseases.contains(it.cancerSite)
				}
			}
			println "studies, $studiesToSearch and diseases = $diseases"	
		}
		
		def outcome = [:]
		outcome["outcomeType"] = params.outcome
		results << outcome
		
		studiesToSearch.each{ study ->
			StudyContext.setStudy(study.schemaName)
			session.dataTypes = AttributeType.findAll().sort { it.longName }
			def result = [:]
			if(params.outcome){
				//semantically resolve criteria purposes across studies
				def outcomeCriteria = []
				def outcomeType
				outcomeCriteria = SemanticHelper.resolveAttributesForStudy(params,study.shortName)
				if(outcomeCriteria){
					//create my 2 groups of outcome
					def patientsLess5 = []
					def patientsMore5 = []

				    //set criteria for lessThan5
					def criteriaL5 = [:]
					outcomeCriteria[0].each() { key, value -> criteriaL5[key]=value };
					
					//set criteria for moreThan5
					def criteriaM5 = [:]
					outcomeCriteria[1].each() { key, value -> criteriaM5[key]=value };
					
					def biospecimenIds
					if(session.dataTypes.collect { it.target }.contains("BIOSPECIMEN")) {
						def biospecimenCriteria = [:]
						outcomeCriteria[2].each() { key, value -> biospecimenCriteria[key]=value };
						println "bc crit: $biospecimenCriteria"
						if(biospecimenCriteria) {
							println "now $biospecimenCriteria"
							biospecimenIds = biospecimenService.queryByCriteria(biospecimenCriteria).collect { it.id }
							println "GOT IDS ${biospecimenIds}"
						}
					}
					
				    //get lessThanPatients
					patientsLess5 = quickSearchForPatients(criteriaL5,biospecimenIds)
					println "patients with Less in $patientsLess5"
					
					//get moreThanPatients
					patientsMore5 = quickSearchForPatients(criteriaM5,biospecimenIds)
					println "patients with more in $patientsMore5"
					
					
					//organize results
					def outcomeLabels = SemanticHelper.determineOutcomeLabel(params.outcome,study.shortName)
					result["study"] = study.shortName	
					result["patients_lessThan"] = patientsLess5.size().toString()
					result["patients_lessThanLabel"] = patientsLess5.size().toString() + ":" + outcomeLabels[0]
					result["patients_moreThan"] = patientsMore5.size().toString()
					result["patients_moreThanLabel"] = patientsMore5.size().toString() + ":" + outcomeLabels[1]
					results << result
				}else {
					println "no semantic match of $params.outcome for $study.shortName"
				}
		   }
		}
		println results.flatten() as JSON
		render results as JSON
	}
	
	def quickSearchForPatients(criteria,biospecimenIds){
		def patients = []
		
		println "quick search criteria is $criteria"
		patients = clinicalService.getPatientIdsForCriteria(criteria, biospecimenIds)
		return patients
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
