class StudyDataSource {

	static mapping = {
		table 'COMMON.DATA_SOURCE'
		version false
		id column:'data_source_id'
	}
	String shortName
	String longName
	String schemaName
}
