class GeneRelationshipEvidence {
	
	static mapping = {
		table 'GENE_RELATIONSHIP_EVIDENCE'
		version false
		id column:'ID'
		relationship column: 'RELATIONSHIP_ID'
		evidenceId column: 'EVIDENCE_ID'
		evidenceSource column: 'EVIDENCE_SOURCE'
	}
	
	GeneRelationship relationship
	
	String evidenceId
	String evidenceSource
}
