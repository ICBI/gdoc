import grails.converters.JSON

class SummaryService {
	def jdbcTemplate 
	def middlewareService
	
	def patientSummary() {
		return jdbcTemplate.queryForInt("select count(distinct(gdoc_id)) from common.patient_data_source")
	}
	
	def studySummary() {
		return StudyDataSource.count()
	}
	
	def sampleSummary() {
		return middlewareService.loadResource("Sample", null, null)
	}
	
	def patientCounts(){
		def roles = jdbcTemplate.queryForList("SELECT * FROM SESSION_ROLES")
		log.debug "ROLES: " + roles
		def studyList = StudyDataSource.findAll()
		def studyCounts =[:]
		def total = 0
		studyList.each{
			try {
				if(it.schemaName != 'PREOP' && it.schemaName != 'EDINFAKE' && it.schemaName != 'DRUG'){
						StudyContext.setStudy(it.schemaName)
						def query = "select count(p) from Patient p"
						def patients = Patient.executeQuery(query)
						if(patients){
							//println patients[0]
							studyCounts[it.shortName] = patients[0]
							total += patients[0]
						}
				}
			} catch(Exception e) {
				log.debug "Got exception: ", e
			}
		}
		studyCounts["Total"] = total
		StudyContext.setStudy(null)
		return studyCounts
	}
	
	def anatomicSources(sampleSummary){
		def anatomicSourcesList = [];
		def anatomicSources = [];
		return anatomicSources
	}
	
}