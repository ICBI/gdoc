class AnalysisCommand {

	String[] groups
	String pvalue
	String foldChange
	String dataFile
	String study
	AnalysisType requestType = (AnalysisType.CLASS_COMPARISON)
	
	static constraints = {
		groups(validator: { val, obj ->
			println "VALUES:  ${val}"
			if(!val) {
				return "custom.size"
			}
			if(val.size() != 2) {
				return "custom.size"
			}
			val.each {list ->
				println val
			}
			return true
		})
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