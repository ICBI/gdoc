class GeneAlias {
	static mapping = {
		table 'GENE_SYMBOL'
		version false
		id column:'GENE_SYMBOL_ID'
		geneId column: 'GENE_ID'
	}
	String symbol
	Long geneId
	Boolean official
	
	static hasMany = [proteins: Protein]
	static fetchMode = [proteins:'eager']
}
