class MicroarrayFile {
	static mapping = {
		table '__STUDY_SCHEMA__.HT_FILE'
		version false
		id column:'ht_file_id', generator: 'sequence', params: [sequence: '__STUDY_SCHEMA__.HT_FILE_SEQUENCE']
		relativePath column: 'relative_path'
		fileSize column: 'size_b'
		dataLevel column: 'data_level'
		insertUser column: 'insert_user'
		insertMethod column: 'insert_method'
		insertDate column: 'insert_date'
		fileType column: 'file_type_id'
		fileFormat column: 'file_format_id'
		priorFiles joinTable:[name:'__STUDY_SCHEMA__.HT_FILE_PRIOR', key:'HT_FILE_ID', column:'PRIOR_HT_FILE_ID']
		
	}
	static hasMany   = [ priorFiles: MicroarrayFile ]
    
	String name
	String description
	String relativePath
	Long fileSize
	String dataLevel
	String insertUser
	String insertMethod
	Date insertDate
	FileType fileType
	FileFormat fileFormat
	
	
}
