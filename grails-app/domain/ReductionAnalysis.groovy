class ReductionAnalysis {
	
	static mapping = {
		table '__STUDY_SCHEMA__.REDUCTION_ANALYSIS'
		version false
	}
	
	String algorithm
	String name
	String algorithmType
	static belongsTo = Biospecimen
	Biospecimen biospecimen
	static hasMany = [locationValues: LocationValue]
	Date dateCreated
	String designType
}