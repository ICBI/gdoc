class GeneExpressionCommand {
	
	List groups
	String geneName
	String dataFile = "EdinPlier_22APR2009.Rda"
	String requestType = "geneExpression"
	def fileBasedAnnotationService
	
	static constraints = {
		groups(validator: { val, obj ->
			if(!val) {
				return "custom.size"
			}
			return true
		})
		geneName(blank:false, validator: {val, obj ->
			def reporters = obj.fileBasedAnnotationService.findReportersForGene(val)
			if(!reporters) {
				return "custom.gene"
			}
			return true
		})
	}
	
}