class LocationValue {
	
	static mapping = {
		table '__STUDY_SCHEMA__.LOCATION_VALUE'
		version false
	}
	
	String chromosome
	Double startPosition
	Double endPosition
	Double value
	static belongsTo = ReductionAnalysis
	ReductionAnalysis reductionAnalysis
}