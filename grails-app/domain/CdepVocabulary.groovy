class CdepVocabulary {
	static mapping = {
		table 'COMMON.CDEP_VOCABULARY'
		version false
		id column:'cdep_vocabulary_id'
		type column:'cdep_type_id'
		
	}
	
	CdepType type
	String term
	String termMeaning
}
