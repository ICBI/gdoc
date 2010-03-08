class MicroarrayFile {
	static mapping = {
		table '__STUDY_SCHEMA__.HT_FILE'
		version false
		id column:'ht_file_id'
		fileType column: 'file_type_id'
	}
	String name
	String description
	FileType fileType
	
}
