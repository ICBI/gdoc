class InteractionService {
	
	def getInteractions(geneSymbol) {
		def relationships = GeneRelationship.findAllByGeneSymbol(geneSymbol)
		def sifItems = []
		def edgeItems = []
		def nodeItems = []
		def geneItems = []
		relationships.each { rel ->
			if(rel.evidence.size() >= 1){
				def concept = rel.conceptName.replaceAll(" ","_")
				def temp = [geneSymbol, "pubmed-relationship", concept]
				sifItems << temp
				
				def relationshipIds = rel.evidence.collect { it.evidenceId }.join(',')
				//println relationshipIds
				def pubMedUrl = "http://www.ncbi.nlm.nih.gov/pubmed/${relationshipIds}"
				def temp2 = [geneSymbol, "(pubmed-relationship)", concept, "=", pubMedUrl]
				edgeItems << temp2
				
				nodeItems << [geneSymbol,"=","Gene"]
				geneItems << [geneSymbol,"=","http://www.genecards.org/cgi-bin/carddisp.pl?gene="+geneSymbol]
				if(rel.conceptType=='COMPOUND'){
					nodeItems << [concept,"=","COMPOUND"]
				}else if (rel.conceptType=='DISEASE'){
					nodeItems << [concept,"=","DISEASE"]
				}else {
					nodeItems << [concept,"=","Concept"]
				}
			}
		}
		return ["sifItems":sifItems,"edgeItems":edgeItems,"nodeItems":nodeItems,"geneItems":geneItems]
	}
	
}