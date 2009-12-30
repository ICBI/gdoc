class GeneAlias {
	static mapping = {
		table 'GENE_SYMBOL'
		version false
		id column:'GENE_SYMBOL_ID'
	}
	
	static belongsTo = Gene
	
	String symbol
	Gene gene
	Boolean official
	
	
}
