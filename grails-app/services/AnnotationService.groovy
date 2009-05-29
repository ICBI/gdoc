class AnnotationService {
	
	
	def findReportersForGene(gene) {
		def criteria = Reporter.createCriteria()
		def reporters = criteria.list{
			eq("geneSymbol", gene.toUpperCase())
		}
		def reporterNames = reporters.collect {
			it.name
		}
		println "REPORTERS ${reporterNames}"
		return reporterNames
	}
	
}