class PcaCommand {

	String[] groups
	String dataFile
	String foldChange
	String variance
	String geneList
	String reporterList
	String reporterCriteria
	String study
	AnalysisType requestType = (AnalysisType.PCA)
	
	static constraints = {
/*		foldChange(validator: {val, obj ->
			if(obj.reporterCriteria == 'foldChange') {
				if(!val)
					return "blank"
				if(!val.isDouble())
					return "custom.number"
			}
			return true
		})
		variance(validator: {val, obj ->
			if(obj.reporterCriteria == 'variance') {
				if(!val)
					return "blank"
				if(!val.isInteger())
					return "custom.number"
				if(val.toInteger() < 1 || val.toInteger() > 99) 
					return "custom.range"
			}
			return true
		})*/
	}
	
}