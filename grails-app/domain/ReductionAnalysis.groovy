class ReductionAnalysis {
	
	static mapping = {
		table '__STUDY_SCHEMA__.REDUCTION_ANALYSIS'
		version false
	}
	
	String algorithm
	String name
	String algorithmType
	static belongsTo = Patient
	Patient patient
	static hasMany = [locationValues: LocationValue]
	Date dateCreated
}