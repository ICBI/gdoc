class DrugDiscoveryService {
	def annotationService
	
	/**
	Currently finds all proteins for a gene alias
	**/
	def findProteinsFromAlias(alias) {
		def geneAlias = annotationService.findGeneByAlias(alias)
		if(geneAlias){
			def gene = geneAlias.gene
			//println "Gene alias $geneAlias.symbol has gene"
			if(gene){
				def proteins = []
				proteins = gene.proteins
				//println "Gene $gene.title transcribes proteins"
				if(proteins && proteins.bindings){
					println "bindings were found from $geneAlias"
					return proteins.collect{it.name}
				}else return false
			}else return false
		}else return false
		
    }
	
	
}