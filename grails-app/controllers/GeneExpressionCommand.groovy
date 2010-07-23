class GeneExpressionCommand {
	
	String[] groups
	String geneName
	String dataFile
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
			def reporters = obj.annotationService.findReportersForGene(val)
			if(!reporters) {
				return "custom.gene"
			}
			return true
		})
		dataFile(blank:false)
	}
	
}