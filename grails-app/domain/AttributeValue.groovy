class AttributeValue {
	static mapping = {
		table '__STUDY_SCHEMA__.patient_attribute_value'
		version false
		id column:'PATIENT_ATTRIBUTE_VALUE_ID', generator: 'sequence', params: [sequence: '__STUDY_SCHEMA__.PATIENT_ATTRIB_VAL_SEQUENCE']
		patient column:'patient_id', insertable: false, updateable: false
		studyPatient column: 'patient_id'
		type column:'attribute_type_id', insertable: false, updateable: false
		commonType column:'attribute_type_id'
	}

	String value
	Patient patient
	StudyPatient studyPatient
	AttributeType type
	CommonAttributeType commonType
	String insertUser
	String insertMethod
	Date insertDate
}
