class AttributeVocabulary {
	static mapping = {
		table 'COMMON.ATTRIBUTE_VOCABULARY'
		version false
		id column:'attribute_vocabulary_id'
		type column:'attribute_type_id'
		
	}
	
	AttributeType type
	String term
	String termMeaning
}
