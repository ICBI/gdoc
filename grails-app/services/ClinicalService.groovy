import org.springframework.jdbc.core.JdbcTemplate 
import groovy.text.SimpleTemplateEngine

class ClinicalService {
	static PAGE_SIZE = 1000
	def sessionFactory
	def jdbcTemplate 
	def queryString = '(select p.patient_id from edin.patient p, common.attribute_type c, edin.dec_value v ' +
		 			  'where p.patient_id = v.patient_id and v.attribute_type_id = c.attribute_type_id ' +
					  ' and v.value = \'$value\' and c.short_name = \'$key\')'
	def rangeQueryString = '(select p.patient_id from edin.patient p, common.attribute_type c, edin.dec_value v ' +
		 			  	   'where p.patient_id = v.patient_id and v.attribute_type_id = c.attribute_type_id ' +
					  	   ' and c.short_name = \'$key\' and v.value BETWEEN $value.min and $value.max )'					
	
    boolean transactional = true
	
	def queryByCriteria(criteria) {
		def engine = new SimpleTemplateEngine()
		def queryTemplate = engine.createTemplate(queryString)
		def rangeQueryTemplate = engine.createTemplate(rangeQueryString)
		def selects = []
		criteria.each { entry ->
			def temp =[:]
			temp.key = entry.key
			temp.value = entry.value
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
