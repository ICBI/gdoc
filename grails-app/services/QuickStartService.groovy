class QuickStartService {
	def clinicalService
	def biospecimenService
	def htDataService
	def jdbcTemplate 
	
	def getDataAvailability(studies){
		println "get all study availability"
		def vocabList = [:]
		def attList = [""]
		def results = []
		if(studies) {
			//get all diseases
			def diseases = []
			diseases = studies.collect{it.cancerSite}
			diseases.remove("N/A")
			vocabList["diseases"] = diseases as Set
			//get all datatypes
			def allDataTypes = []
			allDataTypes = htDataService.getAllHTDataTypes()
			vocabList["allDataTypes"] = allDataTypes as Set
			studies.each{ study ->
				//println "gather atts for $study"
				
					StudyContext.setStudy(study.schemaName)
					if(study.content){
						StudyContext.setStudy(study.schemaName)
						def result = queryStudyData(study,allDataTypes)
						if(result){
							results << result
						}
					}
				
			}
			vocabList["dataAvailability"] = results
		}
		return vocabList
	}
	
	def queryStudyData(study, allDataTypes){
		StudyContext.setStudy(study.schemaName)
		def result = [:]
		def studyName = [:]
	
		
		//find all patients (clnicalData)
		def patients = []
		patients = Patient.findAll()
		result['STUDY'] = study.shortName
		println "total patients in study: " + patients.size()
		result['CLINICAL'] = patients.size()
		
		
		
		allDataTypes.each{ type ->
			if(type != 'CLINICAL'){
				println "find if $type data in $study"
				//get specimens
				def samples = []
				def reductionAnalyses = []
				samples = Sample.findAllByDesignType(type)
				reductionAnalyses = ReductionAnalysis.findAllByDesignType(type)
				//get biospecs
				def pw = []
				if(samples){
					def bsWith = []
					def sids = []
					sids = samples.collect{it.id}
					def sidsString = sids.toString().replace("[","")
					sidsString = sidsString.replace("]","")
					def query = "select s.biospecimen_id from " + study.schemaName + ".HT_FILE_CONTENTS s where s.id in ("+sidsString+")"
					bsWith = jdbcTemplate.queryForList(query)
					println "biospecimens after $bsWith"
					def bsIds = bsWith.collect { id ->
						return id["BIOSPECIMEN_ID"]
					}
					//println bsIds
					def biospecimens = []
					biospecimens = Biospecimen.getAll(bsIds)
					//get patients
					def patientWith = []
					patientWith = biospecimens.collect{it.patient.id}
					println patientWith
					if(patientWith){
						pw = Patient.getAll(patientWith) as Set
						//println "all patients with $type: " + pw.size() + " " + pw
					}
				}
				if(reductionAnalyses){
					def bsWithRA = []
					def rids = []
					rids = reductionAnalyses.collect{it.id}
					def ridsString = rids.toString().replace("[","")
					ridsString = ridsString.replace("]","")
					def rquery = "select s.biospecimen_id from " + study.schemaName + ".REDUCTION_ANALYSIS s where s.id in ("+ridsString+")"
					bsWithRA = jdbcTemplate.queryForList(rquery)
					println "biospecimens with RA $bsWithRA"
					def bsRids = bsWithRA.collect { id ->
						return id["BIOSPECIMEN_ID"]
					}
					//println bsRids
					def biospecimensWithRA = []
					biospecimensWithRA = Biospecimen.getAll(bsRids)
					//get patients
					def patientWithRA = []
					patientWithRA = biospecimensWithRA.collect{it.patient.id}
					println patientWithRA
					if(patientWithRA){
						pw = Patient.getAll(patientWithRA) as Set
						//println "all patients with $type: " + pw.size() + " " + pw
					}
				}
				def stats = [:]
				result[type] = pw.size()
			}
		}
		return result
	}
	
	def queryOutcomes(outcomeParams, study, dataTypes){
		/** @TODO: refactor, along with SemanticHelper to either 
		1)run canned query (or access view) and utilze existing clinicalService to add criteria onto precanned outcome of patientIds
		2)JUST refactor SemanticHelper to resolve come outcomes and outcomesTimes 
		**/
		def outcomeCriteria = []
		def outcomeType
		def results = []
		def result = [:]
		outcomeCriteria = SemanticHelper.resolveAttributesForStudy(outcomeParams,study.shortName)
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
			if(dataTypes.collect { it.target }.contains("BIOSPECIMEN")) {
				def biospecimenCriteria = [:]
				outcomeCriteria[2].each() { key, value -> biospecimenCriteria[key]=value };
				if(biospecimenCriteria) {
					biospecimenIds = biospecimenService.queryByCriteria(biospecimenCriteria).collect { it.id }
					println "GOT IDS ${biospecimenIds}"
				}
			}
			
		    //get lessThanPatients
			def patLTIds = []
			patLTIds = clinicalService.getPatientIdsForCriteria(criteriaL5,biospecimenIds)
			println "get gdocIds for less"
			patientsLess5 = Patient.getAll(patLTIds).collect{it.gdocId}
			//println "patients with Less in $patientsLess5"
			
			//get moreThanPatients
			def patMTIds = []
			patMTIds= clinicalService.getPatientIdsForCriteria(criteriaM5, biospecimenIds)
			println "get gdocIds for more than"
			patientsMore5 =  Patient.getAll(patMTIds).collect{it.gdocId}
			//println "patients with more in $patientsMore5"
			
			
			//organize results
			def outcomeLabels = SemanticHelper.determineStudyDataLabel(outcomeParams.outcome,study.shortName)
			result["study"] = study.shortName
			result["patients_lessThan"]	= patientsLess5
			result["patients_lessThanSize"] = patientsLess5.size().toString()
			result["patients_lessThanLabel"] = patientsLess5.size().toString() + ":" + outcomeLabels[0]
			result["patients_moreThan"] = patientsMore5
			result["patients_moreThanSize"] = patientsMore5.size().toString()
			result["patients_moreThanLabel"] = patientsMore5.size().toString() + ":" + outcomeLabels[1]
			//results << result
		}else {
			println "no semantic match of $outcomeParams.outcome for $study.shortName"
		}
		
		return result
	}
	
}