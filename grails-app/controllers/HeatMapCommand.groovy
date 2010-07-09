class HeatMapCommand {
	
	String patientList
	String reporterList
	String geneList
	String study
	String statisticalMethod
	String dataFile
	AnalysisType requestType = (AnalysisType.HEATMAP)
	
	static constraints = {
		patientList(blank:false)
		geneList(validator: {val, obj ->
			if(val && obj.properties['reporterList']){
					return "custom.val"
			}else if(!val && !obj.properties['reporterList']){
					return "custom.val"
			}else {
				return true
			}
		})
		reporterList(validator: {val, obj ->
			if(val && obj.properties['geneList']){
					return "custom.val"
			}else if(!val && !obj.properties['geneList']){
					return "custom.val"
			}else {
				return true
			}
		})
	}
	
}