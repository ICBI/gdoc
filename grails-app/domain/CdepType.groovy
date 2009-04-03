class CdepType {
	static mapping = {
		table 'COMMON.CDEP_TYPE'
		version false
		id column:'cdep_type_id'
		vocabs column: 'cdep_type_id'
	}
	static fetchMode = [vocabs:"eager"]
	static hasMany = [vocabs : CdepVocabulary]
	
	String shortName
	String longName
	String definition
	String cdep
	Boolean qualitative
	Boolean continuous
	Boolean vocabulary
}
