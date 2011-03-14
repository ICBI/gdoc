import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes

class QuickStartService implements ApplicationContextAware{
	def clinicalService
	def biospecimenService
	def htDataService
	def outcomeDataService
	def jdbcTemplate 
	def applicationContext
	
	void setApplicationContext(ApplicationContext context) { this.applicationContext = context.getServletContext() }
	
	def queryStudyData(study, allDataTypes){
		StudyContext.setStudy(study.schemaName)
		def result = [:]
		def studyName = [:]
	
		
		//find all patients (clnicalData)
		def patients = []
		patients = Patient.findAll()
		result['STUDY'] = study.shortName
		result['CANCER'] = study.cancerSite
		log.debug "find data for $study.shortName -> total patients in study: " + patients.size()
		result['CLINICAL'] = patients.size()
		
		
		
		allDataTypes.each{ type ->
			if(type != 'CLINICAL'){
				//log.debug "find if $type data in $study.shortName"
				//get specimens
				def samples = []
				samples = Sample.findAllByDesignType(type)
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
					//log.debug "biospecimens after $bsWith"
					def bsIds = bsWith.collect { id ->
						return id["BIOSPECIMEN_ID"]
					}
					//log.debug bsIds
					def biospecimens = []
					//biospecimens = Biospecimen.getAll(bsIds)
					//get patients
					def patientWith = []
					def bidsString = bsIds.toString().replace("[","")
					bidsString = bidsString.replace("]","")
					def query2 = "select b.patient_id from " + study.schemaName + ".BIOSPECIMEN b where b.biospecimen_id in ("+bidsString+")"
					patientWith = jdbcTemplate.queryForList(query2);//biospecimens.collect{it.patient.id}
					def patIds = patientWith.collect { id ->
						return id["PATIENT_ID"]
					}
					//log.debug "returned patient ids=" + patIds
					if(patIds){
						pw = Patient.getAll(patIds) as Set
						//log.debug "all patients with $type: " + pw.size() + " " + pw
					}
				}
				
				def stats = [:]
				result[type] = pw.size()
			}
		}
		return result
	}
	
	def queryOutcomes(outcomeParams,studiesToSearch, dataAvailable){
		/** @TODO: refactor, along with SemanticHelper to either 
		1)run canned query (or access view) and utilze existing clinicalService to add criteria onto precanned outcome of patientIds
		2)JUST refactor SemanticHelper to resolve come outcomes and outcomesTimes 
		**/
		def outcomeCriteria = []
		def outcomeType
		def results = []
		
		def lessResults = Outcome.findAllByOutcomeType(outcomeParams[0])
		def moreResults = Outcome.findAllByOutcomeType(outcomeParams[1])
		
		studiesToSearch.each{ study ->
			def result = [:]
			result["study"] = study.shortName
			def da = dataAvailable.find{ studyDa ->
				studyDa['STUDY'] == study.shortName
			}
			result["dataBreakdown"] = da
			def patientsLess = []
			def outcomeLessLabel = "No label"
			def outcomeMoreLabel = "No label"
			def patientsMore = []
			if(lessResults){
				def outcomesL = []
				outcomesL = lessResults.findAll{ outcome ->
					study.id == outcome.studyDataSource.id
				}
				if(outcomesL){
					outcomeLessLabel = outcomesL[0].outcomeDescription
					patientsLess = outcomesL.collect{it.patientId}
				}
			
			}
			
			if(moreResults){
				def outcomesM = []
				outcomesM = moreResults.findAll{ outcome ->
					study.id == outcome.studyDataSource.id
				}
				if(outcomesM){
					outcomeMoreLabel = outcomesM[0].outcomeDescription 
					patientsMore = outcomesM.collect{it.patientId}
				}
			}
			
			if(patientsLess || patientsMore){
				log.debug("found data available for $study.shortName")
				result["patients_lessThan"]	= patientsLess
				result["patients_lessThanSize"] = patientsLess.size().toString()
				result["patients_lessThanLabel"] = " " + patientsLess.size().toString() + " patients :" + outcomeLessLabel

				result["patients_moreThan"] = patientsMore
				result["patients_moreThanSize"] = patientsMore.size().toString()
				result["patients_moreThanLabel"] = " " + patientsMore.size().toString() + " patients :" + outcomeMoreLabel
				results << result
			}
			else{
				log.debug("no data available for $study.shortName")
			}
			
		}
	
		
		return results
	}
	
}