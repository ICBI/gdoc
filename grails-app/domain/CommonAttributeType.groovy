class CommonAttributeType {

	static mapping = {
		table 'COMMON.ATTRIBUTE_TYPE'
		version false
		id column:'attribute_type_id', generator: 'sequence', params: [sequence: 'ATTRIBUTE_TYPE_SEQUENCE']
		vocabs column: 'attribute_type_id'
		value column: 'class'
	}
	static constraints = {
        upperRange(nullable: true)
        lowerRange(nullable: true)
    }
    
	static fetchMode = [vocabs:"eager"]
	static hasMany = [vocabs : AttributeVocabulary]
	
	String shortName
	String longName
	String definition
	String value
	String semanticGroup
	String gdocPreferred
	String cadsrId
	String evsId
	Boolean qualitative
	Boolean continuous
	Boolean vocabulary
	String oracleDatatype
	String unit
	Double upperRange
	Double lowerRange
	String insertUser
	Date insertDate
	String insertMethod
}
