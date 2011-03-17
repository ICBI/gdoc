class PcaCommand {

	String[] groups
	String dataFile
	String dataSetType
	String foldChange
	String variance
	String geneList
	String reporterList
	String reporterCriteria
	String study
	String patientCriteria
	static AnalysisType requestType = (AnalysisType.PCA)
	def annotationService
	def idService
	
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
		reporterList(validator: {val, obj ->
			if(val) {
				def platformReporters 
				if ("METABOLOMICS" == obj.dataSetType) {
					def file = MicroarrayFile.findByName(obj.dataFile)
					def peakNames = file.peaks.collect { peak ->
						return peak.name
					}
					platformReporters = peakNames
				} else {
					platformReporters = obj.annotationService.findReportersForFile(obj.dataFile)
				}
				def reporters = obj.idService.reportersForListName(val)
				platformReporters.retainAll(reporters)
				reporters = platformReporters
				if(!reporters) {
					return "custom.reporters"
				}
			}
			return true
		})
	}
}