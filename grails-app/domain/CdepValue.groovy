class CdepValue {
	static mapping = {
		table '__STUDY_SCHEMA__.CDEP_VALUE'
		version false
		id column:'cdep_value_id'
		patient column:'patient_id'
		type column:'cdep_type_id'
	}

	String value
	Patient patient
	CdepType type
}
