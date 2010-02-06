class FileType {
	static mapping = {
		table 'COMMON.FILE_TYPE'
		version false
		id column:'file_type_id'
	}
	static searchable = true 
	String name
}
