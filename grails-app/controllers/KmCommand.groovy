class KmCommand {

	String[] groups
	String endpoint
	AnalysisType requestType = (AnalysisType.KM_PLOT)
	//the following are strictly for KM based on gene expression
	List reporters
	String currentReporter
	Integer foldChange
	Integer geAnalysisId
	
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