class GeneExpressionCommand {
	
	String[] groups
	String geneName
	String dataFile
	List reporters
	AnalysisType requestType = (AnalysisType.GENE_EXPRESSION)
	def annotationService
	String study
	
	static constraints = {
		groups(validator: { val, obj ->
			if(!val) {
				return "custom.size"
			}
			return true
		})
		geneName(blank:false, validator: {val, obj ->
			obj.reporters = obj.annotationService.findReportersForGeneAndFile(val, obj.dataFile)
			if(!obj.reporters) {
				return "custom.gene"
			}
			return true
		})
		dataFile(blank:false)
	}
	
}