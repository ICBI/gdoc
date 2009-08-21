class GeneRelationship {
	
	static mapping = {
		table 'GENE_RELATIONSHIP'
		version false
		id column:'RELATIONSHIP_ID'
		geneSymbol column: 'HUGO_GENE_SYM'
		entrezId column: 'ENTREZ_GENE_ID'
		conceptName column: 'CONCEPT_NAME'
		conceptCode column: 'EVS_CONCEPT_CODE'
		conceptType column: 'CONCEPT_TYPE'
		source column: 'RELATIONSHIP_SOURCE'
	}
	
	static fetchMode = [evidence:"eager"]
	static hasMany = [evidence:GeneRelationshipEvidence]
	
	String geneSymbol
	String entrezId
	String conceptName
	String conceptCode
	String conceptType
	String source
}
