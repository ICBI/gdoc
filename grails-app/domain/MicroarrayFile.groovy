class MicroarrayFile {
	static mapping = {
		table '__STUDY_SCHEMA__.MARRAY_FILE'
		version false
		id column:'marray_file_id'
		fileType column: 'file_type_id'
	}
	String name
	FileType fileType
	
}
