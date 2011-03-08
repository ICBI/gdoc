class MicroarrayFile {
	static mapping = {
		table '__STUDY_SCHEMA__.HT_FILE'
		version false
		id column:'ht_file_id', generator: 'sequence', params: [sequence: '__STUDY_SCHEMA__.HT_FILE_SEQUENCE']
	}
	static hasMany   = [ peaks: MSPeak ]
	static mappedBy = [ peaks: "file"]

	String name
	String description
}
