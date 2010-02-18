class StudyPatient {

	static mapping = {
		table '__STUDY_SCHEMA__.PATIENT'
		version false
		id column: 'patient_id', generator: 'sequence', params: [sequence: 'PATIENT_SEQUENCE']
		values column:'patient_id'
	}
	
	static hasMany = [values : AttributeValue]
	Long id
	String dataSourceInternalId 
	String insertUser
	String insertMethod
	Date insertDate
}
