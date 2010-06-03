class KmGeneExpCommand {

	String groups
	String endpoint
	AnalysisType requestType = (AnalysisType.KM_GENE_EXPRESSION)
	List reporters
	String currentReporter
	Double foldChange
	Integer geAnalysisId
	String geneName
	String dataFile
	def annotationService
	String study
	
	static constraints = {
		groups(blank:false)
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