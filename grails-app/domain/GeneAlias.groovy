class GeneAlias {
	static mapping = {
		table 'GENE_SYMBOL'
		version false
		id column:'GENE_SYMBOL_ID'
	}
	
	static searchable = {
		alias "alias"
	    gene reference: true
		official index: 'no'
	}
	
	static belongsTo = Gene
	
	String symbol
	Gene gene
	Boolean official
	
	
}
