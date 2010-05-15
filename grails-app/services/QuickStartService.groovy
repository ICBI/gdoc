class QuickStartService {
	def clinicalService
	def biospecimenService
	def htDataService
	def jdbcTemplate 
	
	def queryStudyData(dataParams, study){
		StudyContext.setStudy(study.schemaName)
		def result = [:]
		result["study"] = study.shortName
		//find all patients (clnicalData)
		def patients = []
		patients = Patient.findAll()
		println "total patients: " + patients.size()
		result["clinical"] = patients.size()
		
		//find all array specimens
		def types = []
		if(dataParams.type == 'ALL'){
			types = htDataService.getAllHTDataTypes()
		}
		else{
			if(dataParams.type.metaClass.respondsTo(dataParams.type,"max")){
				types = dataParams.type as List
			}else{
				types << dataParams.type
			}
		}
		
		println "find if data available for $types"
		/**find all XYZ DATA**/
		types.each{ type ->
			//get specimens
			def samples = []
			samples = Sample.findAllByDesignType(type)
			println "samples after "+ samples
			//get biospecs
			def bsWith = []
			def pw = []
			def sids = []
			if(samples){
				sids = samples.collect{it.id}
				def sidsString = sids.toString().replace("[","")
				sidsString = sidsString.replace("]","")
				//get biospecsl("from Sample as s where s.id in (:p)", [p:plist])
				def query = "select s.biospecimen_id from " + study.schemaName + ".HT_FILE_CONTENTS s where s.id in ("+sidsString+")"
				bsWith = jdbcTemplate.queryForList(query)
				println "biospecimens after $bsWith"
				def bsIds = bsWith.collect { id ->
					return id["BIOSPECIMEN_ID"]
				}
				println bsIds
				def biospecimens = []
				biospecimens = Biospecimen.getAll(bsIds)
				//get patients
				def patientWith = []
				patientWith = biospecimens.collect{it.patient.id}
				println patientWith
				if(patientWith){
					pw = Patient.getAll(patientWith) as Set
					println "all patients with $type: " + pw.size() + " " + pw
				}
			}
			result[type] = pw.size()
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
			patientsLess5 = clinicalService.getPatientIdsForCriteria(criteriaL5,biospecimenIds)
			//println "patients with Less in $patientsLess5"
			
			//get moreThanPatients
			patientsMore5 = clinicalService.getPatientIdsForCriteria(criteriaM5, biospecimenIds)
			//println "patients with more in $patientsMore5"
			
			
			//organize results
			def outcomeLabels = SemanticHelper.determineStudyDataLabel(outcomeParams.outcome,study.shortName)
			result["study"] = study.shortName	
			result["patients_lessThan"] = patientsLess5.size().toString()
			result["patients_lessThanLabel"] = patientsLess5.size().toString() + ":" + outcomeLabels[0]
			result["patients_moreThan"] = patientsMore5.size().toString()
			result["patients_moreThanLabel"] = patientsMore5.size().toString() + ":" + outcomeLabels[1]
			//results << result
		}else {
			println "no semantic match of $outcomeParams.outcome for $study.shortName"
		}
		
		return result
	}
	
}