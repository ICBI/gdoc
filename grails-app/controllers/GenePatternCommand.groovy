class GenePatternCommand {

	List groups
	String dataFile = "EdinPlier_22APR2009.Rda"
	String analysisName
	
	static constraints = {
		groups(validator: { val, obj ->
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
		analysisName(blank:false)
	}
	
}