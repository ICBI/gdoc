class StudyDataSource {

	static mapping = {
		table 'COMMON.DATA_SOURCE'
		version false
		id column:'data_source_id'
		abstractText column: 'abstract'
	}
	String shortName
	String longName
	String abstractText
	String cancerSite
	String piLastName
	String piFirstName
	String piNameSuffix
	String contactLastName
	String contactFirstName
	String schemaName
}
