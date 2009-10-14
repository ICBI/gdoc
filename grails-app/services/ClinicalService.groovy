import org.springframework.jdbc.core.JdbcTemplate 
import groovy.text.SimpleTemplateEngine

class ClinicalService {
	static PAGE_SIZE = 1000
	def sessionFactory
	def jdbcTemplate 
	def queryString = '(select p.patient_id from ${schema}.patient p, common.attribute_type c, ${schema}.patient_attribute_value v ' +
		 			  'where p.patient_id = v.patient_id and v.attribute_type_id = c.attribute_type_id ' +
					  ' and v.value = \'${value}\' and c.short_name = \'${key}\')'
	def rangeQueryString = '(select p.patient_id from ${schema}.patient p, common.attribute_type c, ${schema}.patient_attribute_value v ' +
		 			  	   'where p.patient_id = v.patient_id and v.attribute_type_id = c.attribute_type_id ' +
					  	   ' and c.short_name = \'${key}\' and v.value BETWEEN ${value.min} and ${value.max} )'					
	
	def patientIdQuery = 'select b.patient_id from ${schema}.biospecimen b where b.biospecimen_id in (${ids})'
	def gdocIdQuery = 'select p.patient_id from ${schema}.patients p where p.gdoc_id in (${ids})'
    boolean transactional = true
	
	def queryByCriteria(criteria, biospecimenIds) {
		def engine = new SimpleTemplateEngine()
		def queryTemplate = engine.createTemplate(queryString)
		def rangeQueryTemplate = engine.createTemplate(rangeQueryString)
		def selects = []
		
		// get patient ids for biospecimens
		def bioPatientIds
		if(biospecimenIds && biospecimenIds.size() > 0) {
			bioPatientIds = getPatientIdsForBiospecimenIds(biospecimenIds)
		}
		println "BIO PATIENT IDS $bioPatientIds"
		if(!criteria || criteria.size() == 0) {
			def patients = Patient.executeQuery("select p from Patient p")
			// filter out patients that did not match biopecimens
			if(bioPatientIds && bioPatientIds.size() > 0) {
				patients = patients.findAll {
					println "COMPARING ${it.id}"
					bioPatientIds.count(it.id) > 0
				}
			}
			return patients
		}
		criteria.each { entry ->
			def temp =[:]
			temp.key = entry.key
			temp.value = entry.value
			temp.schema = StudyContext.getStudy()
			if(temp.value instanceof java.util.Map) {
				selects << rangeQueryTemplate.make(temp)
			} else {
				selects << queryTemplate.make(temp)
			}
			
		}
		def query = selects.join(" INTERSECT ")
		println query
		def ids = jdbcTemplate.queryForList(query)
		def patientIds = ids.collect { id ->
			return id["PATIENT_ID"]
		}
		
		// filter out patients that do not match biospecimen criteria
		if(bioPatientIds && bioPatientIds.size() > 0) {
			patientIds = patientIds.intersect(bioPatientIds)
		}
		return getPatientsForIds(patientIds)
	}
	
	def getPatientIdsForBiospecimenIds(biospecimenIds) { 
		
		def engine = new SimpleTemplateEngine()
		def queryTemplate = engine.createTemplate(patientIdQuery)
		def patientIds = []
		def index = 0;
		while(index < biospecimenIds.size()) {
			def specimensLeft = biospecimenIds.size() - index
			def tempSpecimens
			if(specimensLeft > PAGE_SIZE) {
				def ids = biospecimenIds.getAt(index..<(index + PAGE_SIZE))
				def temp =[:]
				temp.ids = ids.join(", ")
				temp.schema = StudyContext.getStudy()
				def query = queryTemplate.make(temp)
				println "QUERY $query"
				tempSpecimens = jdbcTemplate.queryForList(query.toString())
				patientIds.addAll(tempSpecimens.collect { id ->
					return id["PATIENT_ID"]
				})
				index += PAGE_SIZE
			} else {
				def ids = biospecimenIds.getAt(index..<biospecimenIds.size())
				println "IDS $ids"
				
				def temp =[:]
				temp.ids = ids.join(", ")
				temp.schema = StudyContext.getStudy()
				def query = queryTemplate.make(temp)
				println "QUERY $query"
				tempSpecimens = jdbcTemplate.queryForList(query.toString())
				patientIds.addAll(tempSpecimens.collect { id ->
					return id["PATIENT_ID"]
				})
				index += specimensLeft
			}
		}
		return patientIds
	}
	
	def getPatientsForGdocIds(gdocIds) {
		def engine = new SimpleTemplateEngine()
		def queryTemplate = engine.createTemplate(gdocIdQuery)
		def temp =[:]
		temp.ids = gdocIds.join(", ")
		temp.schema = StudyContext.getStudy()
		def query = queryTemplate.make(temp)
		def patientIds = []
		def tempSpecimens = jdbcTemplate.queryForList(query.toString())
		patientIds.addAll(tempSpecimens.collect { id ->
			return id["PATIENT_ID"]
		})
		return getPatientsForIds(patientIds)
	}
	
	def getPatientsForIds(patientIds) {
		def patients = []
		def index = 0;
		println "patient ids $patientIds"
		while(index < patientIds.size()) {
			def patientsLeft = patientIds.size() - index
			def tempPatients
			if(patientsLeft > PAGE_SIZE) {
				tempPatients = Patient.getAll(patientIds.getAt(index..<(index + PAGE_SIZE)))
				patients.addAll(tempPatients)
				index += PAGE_SIZE
			} else {
				tempPatients = Patient.getAll(patientIds.getAt(index..<patientIds.size()))
				patients.addAll(tempPatients)
				index += patientsLeft
			}
		}
		println patients.size()
		return patients.grep { it }
	}
}
