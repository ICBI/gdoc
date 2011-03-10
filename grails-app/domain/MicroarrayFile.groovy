class MicroarrayFile {
	static mapping = {
		table '__STUDY_SCHEMA__.HT_FILE'
		version false
		id column:'ht_file_id', generator: 'sequence', params: [sequence: '__STUDY_SCHEMA__.HT_FILE_SEQUENCE']
		design column: 'design_id'
		subjects joinTable: [name:'__STUDY_SCHEMA__.HT_FILE_SUBJECT', key:'HT_FILE_ID', column:'SUBJECT_ID']
	}
	static constraints = {
        design(nullable: true)
    }
	static hasMany   = [ peaks: MSPeak, subjects: Biospecimen ]
	static mappedBy = [ peaks: "file"]

	String name
	String description
	Design design
}
