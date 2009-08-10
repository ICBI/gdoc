class SummaryService {
	def jdbcTemplate 
	
	def patientSummary() {
		return jdbcTemplate.queryForInt("select count(distinct(gdoc_id)) from common.patient_data_source")
	}
	
	def studySummary() {
		return StudyDataSource.count()
	}
}