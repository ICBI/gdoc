class KmGeneExpCommand {

	String[] groups
	String endpoint
	AnalysisType requestType = (AnalysisType.KM_GENE_EXPRESSION)
	List reporters
	String currentReporter
	Integer foldChange
	Integer geAnalysisId
	String geneName
	String dataFile = "EdinPlier_22APR2009.Rda"
	def annotationService
	
	static constraints = {
		groups(validator: { val, obj ->
			if(!val) {
				return "custom.size"
			}
			return true
		})
		endpoint(blank:false)
		geneName(blank:false, validator: {val, obj ->
			def reporters = obj.annotationService.findReportersForGene(val)
			if(!reporters) {
				return "custom.gene"
			}
			return true
		})
	}
	
}