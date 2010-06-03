class AnalysisCommand {
	
	String baselineGroup
	String groups
	String pvalue
	String foldChange
	String dataFile
	String study
	AnalysisType requestType = (AnalysisType.CLASS_COMPARISON)
	
	static constraints = {
		baselineGroup(blank:false)
		groups(blank:false)
		pvalue(blank:false, validator: {val, obj ->
			if(!val.isDouble()) {
				return "custom.number"
			} else {
				return true
			}
		})
		foldChange(blank:false, validator: {val, obj ->
			if(!val.isDouble()) {
				return "custom.number"
			} else {
				return true
			}
		})
	}
	
}