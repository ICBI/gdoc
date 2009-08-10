class DecValue {
	static mapping = {
		table '__STUDY_SCHEMA__.patient_attribute_value'
		version false
		id column:'PATIENT_ATTRIBUTE_VALUE_ID'
		patient column:'patient_id'
		type column:'attribute_type_id'
	}

	String value
	Patient patient
	AttributeType type
}
