import org.springframework.jdbc.core.JdbcTemplate 
import groovy.text.SimpleTemplateEngine

class ClinicalService {
	static PAGE_SIZE = 1000
	def sessionFactory
	def jdbcTemplate 
	def queryString = '(select p.patient_id from edin.patient p, common.cdep_type c, edin.cdep_value v ' +
		 			  'where p.patient_id = v.patient_id and v.cdep_type_id = c.cdep_type_id ' +
					  ' and v.value = \'$value\' and c.short_name = \'$key\')'
	
    boolean transactional = true
	
	def queryByCriteria(criteria) {
		def engine = new SimpleTemplateEngine()
		def queryTemplate = engine.createTemplate(queryString)
		def selects = []
		criteria.each { entry ->
			def temp =[:]
			temp.key = entry.key
			temp.value = entry.value
			selects << queryTemplate.make(temp)
		}
		def query = selects.join(" INTERSECT ")
		println query
		def ids = jdbcTemplate.queryForList(query)
		def patientIds = ids.collect { id ->
			return id["PATIENT_ID"]
		}
		println patientIds
		def patients = []
		def index = 0;
		while(index < patientIds.size()) {
			def patientsLeft = patientIds.size() - index
			def tempPatients
			println "patients left: " + patientsLeft + " index: " + index
			if(patientsLeft > PAGE_SIZE) {
				println "patients paging: " + patientIds.getAt(index..<(index + PAGE_SIZE))
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
		return patients
	}
}
