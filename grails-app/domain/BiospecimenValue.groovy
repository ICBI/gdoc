class BiospecimenValue {
	static mapping = {
		table '__STUDY_SCHEMA__.biospecimen_attribute_value'
		version false
		id column:'BIOSPEC_ATTRIBUTE_VALUE_ID'
		biospecimen column:'BIOSPECIMEN_ID'
		type column:'attribute_type_id'
	}

	String value
	Biospecimen biospecimen
	AttributeType type
}
