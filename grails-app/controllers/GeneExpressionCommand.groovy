class GeneExpressionCommand {
	
	String[] groups
	String geneName
	String dataFile = "EdinPlier_22APR2009.Rda"
	AnalysisType requestType = (AnalysisType.GENE_EXPRESSION)
	def annotationService
	
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
	}
	
}