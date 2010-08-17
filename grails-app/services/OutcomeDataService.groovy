import org.springframework.jdbc.core.JdbcTemplate 
import groovy.text.SimpleTemplateEngine

class OutcomeDataService {
	def sessionFactory
	def jdbcTemplate
	def clinicalService
    boolean transactional = true

	def addQueryOutcome(study,name, query, description){
		def patientIds = []
		def patients = []
		StudyContext.setStudy(study.schemaName)
		log.debug "Study schema is for $study.schemaName , OUTCOME QUERY: $query"
		patientIds = jdbcTemplate.queryForList(query)
		if(patientIds){
			def pids = patientIds.collect { id ->
				return id["PATIENT_ID"]
			}
			patients = Patient.getAll(pids)
			if(patients){
				def gdocIds = new HashSet()
				gdocIds = patients.collect{it.gdocId}
				log.debug "returned $gdocIds.size() found for query"
				gdocIds.each{ gdocId ->
					def outcomeData = new Outcome(patientId:gdocId,studyDataSource:study, outcomeType:name, outcomeDescription:description)
					if(!outcomeData.save(flush: true)){
						log.error outcomeData.errors
					}
				}
			}
		}else{
			log.debug "No patient ids found for: $query"
		}
	}

    def addQueryOutcomes(bundledSemanticCriteria, study, outcomeLess, outcomeMore, descriptionLess, descriptionMore) {
		def success = true
		def patientsLess5 = []
		def patientsMore5 = []
		StudyContext.setStudy(study.schemaName)
		//set criteria for lessThan5
		def criteriaL5 = [:]
		bundledSemanticCriteria[0].each() { key, value -> criteriaL5[key]=value };

		//set criteria for moreThan5
		def criteriaM5 = [:]
		bundledSemanticCriteria[1].each() { key, value -> criteriaM5[key]=value };

		log.debug "built semantic criteria"
		def dataTypes = AttributeType.findAll().sort { it.longName }
		def biospecimenIds
		if(dataTypes.collect { it.target }.contains("BIOSPECIMEN")) {
			def biospecimenCriteria = [:]
			bundledSemanticCriteria[2].each() { key, value -> biospecimenCriteria[key]=value };
			if(biospecimenCriteria) {
				biospecimenIds = biospecimenService.queryByCriteria(biospecimenCriteria).collect { it.id }
				//log.debug "GOT IDS ${biospecimenIds}"
			}
		}
	
		//get lessThanPatients
		log.debug "query for less than"
		def patLTIds = []
		patLTIds = clinicalService.getPatientIdsForCriteria(criteriaL5,biospecimenIds)
		//log.debug "get gdocIds for less = " + patLTIds
		def patLT = []
		if(patLTIds){
			patLT = Patient.getAll(patLTIds)
		}
	 	
		if(patLT){
			patientsLess5 = patLT.collect{it.gdocId}
		}
		//log.debug "patients with Less in $patientsLess5"
		
		//get moreThanPatients
		log.debug "query for more than"
		def patMTIds = []
		patMTIds= clinicalService.getPatientIdsForCriteria(criteriaM5, biospecimenIds)
		//log.debug "get gdocIds for more than = " + patMTIds
		def patMT = []
		if(patMTIds){
			patMT = Patient.getAll(patMTIds)
		}
		
		if(patMT){
			patientsMore5 = patMT.collect{it.gdocId}
		}
		//log.debug "patients with more in $patientsMore5"
		
		log.debug "insert all patients meeting $outcomeLess criteria"
		patientsLess5.each{ patient ->
			def outcomeDataLess = new Outcome(patientId:patient,studyName:study.shortName, outcomeType:outcomeLess, outcomeDescription:descriptionLess)
			if(!outcomeDataLess.save(flush: true)){
				log.error outcomeDataLess.errors
				success = false
			}
				
		}
		log.debug "insert all patients meeting $outcomeMore criteria"
		patientsMore5.each{ patient ->
			def outcomeDataMore = new Outcome(patientId:patient,studyName:study.shortName, outcomeType:outcomeMore, outcomeDescription:descriptionMore)
			if(!outcomeDataMore.save(flush: true)){
				log.error outcomeDataMore.errors
				success = false
			}
				
		}
	
			
		return success
    }

	
}
