class GeneAliasTests extends GroovyTestCase {
	def annotationService
	
    void testFindTargetsFromAlias() {
		def alias = "EGFR"
		def geneAlias = annotationService.findGeneByAlias(alias)
		if(geneAlias){
			assertTrue(geneAlias.gene != null)
			def gene = geneAlias.gene
			println "Gene alias $geneAlias.symbol has gene"
			if(gene){
				assertTrue(gene.proteins != null)
				def proteins = []
				proteins = gene.proteins
				println "Gene $gene.title transcribes proteins"
				if(proteins){
					println "The following proteins were found from $geneAlias"
					proteins.each{ protein ->
						println "$protein.name "
						if(protein.bindings){
							println " and the following binding molecules were found -> "
							protein.bindings.each{ binding ->
								println "$binding.molecule.name "
							}
						}
					}
				}
			}
		}
		
    }

}
