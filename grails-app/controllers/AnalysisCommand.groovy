class AnalysisCommand {
	
	String baselineGroup
	String groups
	String pvalue
	String foldChange
	String dataFile
	String study
	String statisticalMethod
	String adjustment
	AnalysisType requestType = (AnalysisType.CLASS_COMPARISON)
	def userListService
	
	static constraints = {
		baselineGroup(blank:false)
		groups(blank:false, validator: {val, obj ->
			if(obj.baselineGroup && obj.userListService.doListsOverlap(obj.baselineGroup, val))
				return "custom.overlap"
		})
		dataFile(blank:false)
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