class GeneRelationshipTests extends GroovyTestCase {

    void testFindBySymbol() {
		def genes = GeneRelationship.findAllByGeneSymbol("ABCB1")
		println genes.size()
		genes.each {
			println it.conceptName + " " + it.evidence.evidenceId
		}
    }

}
