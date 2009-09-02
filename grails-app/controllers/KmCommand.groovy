class KmCommand {

	String[] groups
	String endpoint
	String study
	AnalysisType requestType = (AnalysisType.KM_PLOT)
	
	static constraints = {
		groups(validator: { val, obj ->
			if(!val) {
				return "custom.size"
			}
			return true
		})
		endpoint(blank:false)
	}
	
}