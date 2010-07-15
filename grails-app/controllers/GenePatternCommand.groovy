class GenePatternCommand {

	String[] groups
	String dataFile = "EdinPlier_22APR2009.Rda"
	String analysisName
	String geneList
	
	static constraints = {
		groups(validator: { val, obj ->
			if(!val) {
				return "custom.size"
			}
			if(val.size() != 2) {
				return "custom.size"
			}
			return true
		})
		analysisName(blank:false)
	}
	
}