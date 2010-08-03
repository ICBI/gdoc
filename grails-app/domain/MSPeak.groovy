class MSPeak {
	static mapping = {
		table '__STUDY_SCHEMA__.MS_PEAK'
		version false
		id column:'MS_PEAK_ID', generator: 'sequence', params: [sequence: '__STUDY_SCHEMA__.MS_PEAK_SEQUENCE']
		file column:'HT_FILE_ID'
	}

	String name
	String mz
	String retention
	MicroarrayFile file
	String insertUser
	String insertMethod
	Date insertDate
}