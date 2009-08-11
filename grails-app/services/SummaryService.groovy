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
}