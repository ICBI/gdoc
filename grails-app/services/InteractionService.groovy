class InteractionService {
	
	def getInteractions(geneSymbol) {
		def relationships = GeneRelationship.findAllByGeneSymbol(geneSymbol)
		def sifItems = []
		def edgeItems = []
		def nodeItems = []
		relationships.each { rel ->
			if(rel.evidence.size() >= 1){
				def concept = rel.conceptName.replaceAll(" ","_")
				def temp = [geneSymbol, "pubmed-relationship", concept]
				sifItems << temp
				
				def relationshipIds = rel.evidence.collect { it.evidenceId }.join(',')
				println relationshipIds
				def pubMedUrl = "http://www.ncbi.nlm.nih.gov/pubmed/${relationshipIds}"
				def temp2 = [geneSymbol, "(pubmed-relationship)", concept, "=", pubMedUrl]
				edgeItems << temp2
				
				nodeItems << [geneSymbol,"=","Gene"]
				nodeItems << [concept,"=","Concept"]
			}
		}
		return ["sifItems":sifItems,"edgeItems":edgeItems,"nodeItems":nodeItems]
	}
	
}