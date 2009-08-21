class InteractionService {
	
	def getInteractions(geneSymbol) {
		def relationships = GeneRelationship.findAllByGeneSymbol(geneSymbol)
		def items = []
		relationships.each { rel ->
			def relationshipIds = rel.evidence.collect { it.evidenceId }.join(', ')
			println relationshipIds
			def temp = [geneSymbol, rel.conceptName, relationshipIds, rel.evidence.size()]
			items << temp
		}
		return items
	}
	
}