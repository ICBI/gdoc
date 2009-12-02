class PcaCommand {

	String[] groups
	String dataFile
	String foldChange
	String variance
	String geneList
	String reporterList
	String study
	AnalysisType requestType = (AnalysisType.PCA)
	
	static constraints = {
	}
	
}