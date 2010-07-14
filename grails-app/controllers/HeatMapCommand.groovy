class HeatMapCommand {
	
	static Integer MAX_REPORTERS = 5000
	String patientList
	String reporterList
	String geneList
	String study
	String statisticalMethod
	String dataFile
	Boolean fromComparison
	Boolean selectAll
	String reporterIds
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
						def reporters = obj.idService.reportersForListName(val)
						if(!reporters || reporters.size() <=2 || reporters.size() > MAX_REPORTERS) {
							return "custom.reporters"
						}
					}
					return true
				}
			}
		})
		dataFile(blank:false)
	}
	
}