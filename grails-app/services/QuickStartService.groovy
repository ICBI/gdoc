import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH

class QuickStartService implements ApplicationContextAware{
	def clinicalService
	def biospecimenService
	def htDataService
	def outcomeDataService
	def jdbcTemplate 
	def applicationContext
	
	void setApplicationContext(ApplicationContext context) { this.applicationContext = context.getServletContext() }
	
	def getDataAvailability(){
		log.debug "get all study availability"
		def studies = []
		studies = StudyDataSource.list();
		if(studies){
			studies.sort{it.shortName}
		}
		log.debug " sorted studies and getting snapshot for data for all $studies.size studies " + studies.collect{it.shortName}
		def vocabList = [:]
		def attList = [""]
		def results = []
		if(studies) {
			//get all diseases
			def diseases = []
			diseases = studies.collect{it.cancerSite}.sort{it}.unique()
			diseases.remove("N/A")
			vocabList["diseases"] = diseases
			//get all datatypes
			def allDataTypes = []
			allDataTypes = htDataService.getAllHTDataTypes()
			vocabList["allDataTypes"] = allDataTypes as Set
			studies.each{ study ->
				//log.debug "gather atts for $study"
					if(study.shortName != 'TEST-DATA'){
						StudyContext.setStudy(study.schemaName)
						if(study.content){
							StudyContext.setStudy(study.schemaName)
							def result = queryStudyData(study,allDataTypes)
							if(result){
								results << result
							}
						}
					}
				
			}
			vocabList["dataAvailability"] = results
		}
		log.debug "DA: $vocabList"
		return vocabList
	}
	
	def getMyDataAvailability(studies){
		def myDa = [:]
		def appDa
		def servletContext = SCH.servletContext
		if(servletContext){
			appDa = servletContext.getAttribute("dataAvailability")
		}
		else appDa = getDataAvailability()
		myDa["dataAvailability"] = []
		myDa["diseases"] = appDa["diseases"]
		myDa["allDataTypes"] = appDa["allDataTypes"]
		appDa['dataAvailability'].each{elm ->
			def studyName = elm.find{ key, value ->
				if(key == 'STUDY'){
					return value
				}
			}
			studies.each{ myStudy ->
				if(myStudy.shortName == elm['STUDY']){
					myDa["dataAvailability"] << elm
				}
			}
		}
		log.debug "retrieved my data availability"
		return myDa
	}
	
	
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
				if(reductionAnalyses){
					def bsWithRA = []
					def rids = []
					rids = reductionAnalyses.collect{it.id}
					def ridsString = rids.toString().replace("[","")
					ridsString = ridsString.replace("]","")
					def rquery = "select s.biospecimen_id from " + study.schemaName + ".REDUCTION_ANALYSIS s where s.id in ("+ridsString+")"
					bsWithRA = jdbcTemplate.queryForList(rquery)
					//log.debug "biospecimens with RA $bsWithRA"
					def bsRids = bsWithRA.collect { id ->
						return id["BIOSPECIMEN_ID"]
					}
					//log.debug bsRids
					def biospecimensWithRA = []
					biospecimensWithRA = Biospecimen.getAll(bsRids)
					//get patients
					def patientWithRA = []
					patientWithRA = biospecimensWithRA.collect{
						if(it)
							return it.patient.id
					}
					//log.debug patientWithRA
					if(patientWithRA){
						pw = Patient.getAll(patientWithRA) as Set
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
	
		/**
		REFACTORED to use canned queries
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
					//log.debug "GOT IDS ${biospecimenIds}"
				}
			}
			
		    //get lessThanPatients
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
			
			
			//organize results
			def outcomeLabels = SemanticHelper.determineStudyDataLabel(outcomeParams.outcome,study.shortName)
			result["study"] = study.shortName
			result["patients_lessThan"]	= patientsLess5
			result["patients_lessThanSize"] = patientsLess5.size().toString()
			result["patients_lessThanLabel"] = " " + patientsLess5.size().toString() + " patients :" + outcomeLabels[0]
			result["patients_moreThan"] = patientsMore5
			result["patients_moreThanSize"] = patientsMore5.size().toString()
			result["patients_moreThanLabel"] = " " + patientsMore5.size().toString() + " patients :" + outcomeLabels[1]
			//results << result
		}else {
			log.debug "no semantic match of $outcomeParams.outcome for $study.shortName"
		}
		**/
		return results
	}
	
}