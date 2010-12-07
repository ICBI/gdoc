class CinCommand {
	
	String study
	String dataFile
	String cytobandsDataFile
	String cytobandsAnnotationFile = "hg18_annot.Rda"
	String baselineGroup
	String groups
	AnalysisType requestType = (AnalysisType.CIN)
	
	static constraints = {
		dataFile(blank:false)
		cytobandsDataFile(blank:false)
		baselineGroup(blank:false)
		groups(blank:false)
	}
	
}