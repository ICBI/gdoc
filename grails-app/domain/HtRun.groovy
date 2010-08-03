class HtRun {
	static mapping = {
		table '__STUDY_SCHEMA__.HT_RUN'
		version false
		id column:'HT_RUN_ID', generator: 'sequence', params: [sequence: '__STUDY_SCHEMA__.HT_RUN_SEQUENCE']
		design column: 'HT_DESIGN_ID'
		rawFile column: 'RAW_FILE_ID'
		insertUser column: 'INSERT_USER'
		insertMethod column: 'INSERT_METHOD'
		insertDate column: 'INSERT_DATE'
		designTable column: 'HT_DESIGN_TABLE'
	}
    
	String name
	Design design
	MicroarrayFile rawFile
	String insertUser
	String insertMethod
	Date insertDate
	String designTable
}
