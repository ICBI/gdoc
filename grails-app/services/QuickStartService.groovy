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
		log.debug "get all study data availability"
		def studies = []
		studies = StudyDataSource.list();
		if(studies){
			studies.sort{it.shortName}
		}
		log.debug "sorted studies and getting snapshot for data for all $studies.size studies " + studies.collect{it.shortName}
		def vocabList = [:]
		def attList = [""]
		def results = []
		if(studies) {
			//get all diseases
			def diseases = []
			diseases = studies.collect{it.cancerSite}.sort{it}.unique()
			diseases.remove("N/A")
			log.debug "diseases are $diseases"
			vocabList["diseases"] = diseases
			//get all datatypes
			def allDataTypes = []
			allDataTypes = htDataService.getAllHTDataTypes()
			vocabList["allDataTypes"] = allDataTypes as Set
			vocabList["dataAvailability"] = results
			studies.each{ study ->
				if(study.shortName != 'DRUG'){
					def result = [:]
					result['STUDY'] = study.shortName
					result['CANCER'] = study.cancerSite
					log.debug "find data available for $study.shortName"
					def studyDA = []
					studyDA = DataAvailable.findAllByStudyName(study.shortName)
					studyDA.each{ da->
						result[da.dataType] = da.count
					}
					results << result
				}
			}
			vocabList["dataAvailability"] = results
		}
		println "data available " + vocabList
		return vocabList
	}
	
	def loadDataAvailability(){
		log.debug "load all study availability"
		def studies = []
		studies = StudyDataSource.list();
		if(studies){
			studies.sort{it.shortName}
		}
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
								if(result["STUDY"] && result["CANCER"]){
										result.each{ key,value ->
											if(key != "STUDY" && key != "CANCER"){
												def exists = DataAvailable.findByStudyNameAndDataType(result["STUDY"],key)
												if(!exists){
													def da = new DataAvailable(studyName:result["STUDY"],diseaseType:result["CANCER"],dataType:key,count:value)
													if(da.save(flush:true)){
														println "saved $key data for " + result["STUDY"]
													}
												}else{
													exists.count = value
													if(exists.save(flush:true)){
														println "updated $key data for " + result["STUDY"]
													}
												}
											}
										}			
								}
									
							}
						}
					}
				
			}
			vocabList["dataAvailability"] = results
		}
		//log.debug "DA: $vocabList"
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