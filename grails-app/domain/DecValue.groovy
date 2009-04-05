class DecValue {
	static mapping = {
		table '__STUDY_SCHEMA__.DEC_VALUE'
		version false
		id column:'dec_value_id'
		patient column:'patient_id'
		type column:'attribute_type_id'
	}

	String value
	Patient patient
	AttributeType type
}
