class PcaCommand {

	String[] groups
	String dataFile
	String foldChange
	String variance
	String geneList
	String reporterList
	String reporterCriteria
	String study
	String patientCriteria
	AnalysisType requestType = (AnalysisType.PCA)
	
	static constraints = {
		dataFile(blank:false)
		groups(validator: { val, obj ->
			if(obj.patientCriteria == 'ALL')
				return true
			if(!val) {
				return "custom.size"
			}
			return true
		})
	}
	

}