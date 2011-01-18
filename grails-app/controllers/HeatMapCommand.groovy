class HeatMapCommand {
	
	static Integer MAX_REPORTERS = 5000
	String patientList
	String reporterList
	String geneList
	String study
	String statisticalMethod
	String dataFile
	String dataSetType
	Boolean fromComparison
	Boolean selectAll
	String reporterIds
	String[] groups
	AnalysisType requestType = (AnalysisType.HEATMAP)
	def annotationService
	def idService
	
	static constraints = {
		patientList(blank:false)
		geneList(validator: {val, obj ->
			if(!obj.fromComparison) {
				if(val && obj.properties['reporterList']){
						return "custom.val"
				} else if(!val && !obj.properties['reporterList']){
						return "custom.val"
				} else {
					if(val) {
						if ("COPY_NUMBER"== obj.dataSetType || "METABOLOMICS" == obj.dataSetType) {
							return "custom.datatype"
						}
						def reporters = obj.annotationService.findReportersForGeneList(val)
						if(!reporters || reporters.size() <=2 || reporters.size() > MAX_REPORTERS) {
							return "custom.reporters"
						}
					}
					return true
				}
			}
		})
		reporterList(validator: {val, obj ->
			if(!obj.fromComparison) {
				if(val && obj.properties['geneList']){
						return "custom.val"
				} else if(!val && !obj.properties['geneList']){
						return "custom.val"
				} else {
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
						if(!reporters || reporters.size() <=2 || reporters.size() > MAX_REPORTERS) {
							return "custom.reporters"
						}
					}
					return true
				}
			}
		})
		dataFile(blank:false)
		groups(validator: { val, obj ->
			if(obj.patientList == 'ALL')
				return true
			if(!val || val.length > 2) {
				return "custom.size"
			}
			return true
		})
	}
	
}