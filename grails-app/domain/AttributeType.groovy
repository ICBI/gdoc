class AttributeType {
	static mapping = {
		table '__STUDY_SCHEMA__.USED_ATTRIBUTES'
		version false
		id column:'attribute_type_id'
		vocabs column: 'attribute_type_id'
	}
	static fetchMode = [vocabs:"eager"]
	static hasMany = [vocabs : AttributeVocabulary]
	
	String shortName
	String longName
	String definition
	String cdep
	Boolean qualitative
	Boolean continuous
	Boolean vocabulary
}
