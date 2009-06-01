class KmCommand {

	List groups
	String endpoint
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