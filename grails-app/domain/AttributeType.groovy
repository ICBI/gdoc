class AttributeType {
	static mapping = {
		table 'COMMON.ATTRIBUTE_TYPE'
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
