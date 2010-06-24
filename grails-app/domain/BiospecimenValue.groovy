class BiospecimenValue {
	static mapping = {
		table '__STUDY_SCHEMA__.biospecimen_attribute_value'
		version false
		id column:'BIOSPEC_ATTRIBUTE_VALUE_ID', generator: 'sequence', params: [sequence: '__STUDY_SCHEMA__.BIOSPECIMEN_VALUE_SEQUENCE']
		biospecimen column:'BIOSPECIMEN_ID'
		commonType column:'attribute_type_id'
	}

	String value
	Biospecimen biospecimen
	CommonAttributeType commonType
	String insertUser
	String insertMethod
	Date insertDate
}
