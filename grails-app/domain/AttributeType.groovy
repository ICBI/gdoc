class AttributeType {
	static mapping = {
		table '__STUDY_SCHEMA__.USED_ATTRIBUTES'
		version false
		id column:'attribute_type_id'
		vocabs column: 'attribute_type_id'
		value column: 'class'
	}
	
	static fetchMode = [vocabs:"eager"]
	static hasMany = [vocabs : AttributeVocabulary]
	
	String shortName
	String longName
	String definition
	String value
	String target
	Boolean qualitative
	Boolean continuous
	Boolean vocabulary
	Double upperRange
	Double lowerRange
}
