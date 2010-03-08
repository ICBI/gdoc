class CommonPatient {

	static mapping = {
		table 'COMMON.PATIENT_DATA_SOURCE'
		version false
		id column:'gdoc_id', generator: 'sequence', params: [sequence: 'PATIENT_SEQUENCE']
		studyDataSource column: 'data_source_id'
		patient column: 'patient_id'
	}
	
	
	Long id
	StudyPatient patient
	StudyDataSource studyDataSource
	String insertUser
	String insertMethod
	Date insertDate
}
