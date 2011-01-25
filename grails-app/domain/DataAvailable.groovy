class DataAvailable {
	
	static mapping = {
		table 'DATA_AVAILABLE'
		version false
		id column:'ID'
	}

	String studyName
	String diseaseType
	String dataType
	Long count
}
