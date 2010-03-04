class Gene {
	static mapping = {
		table 'GENE'
		version false
		id column:'GENE_ID'
	}
	
	static searchable = {
		alias "gene"
	    geneAliases reference: true
		proteins component: true
	}
	
	String mapLocation
	String title
	String geneType
	String chromosome
	static hasMany = [proteins: Protein, geneAliases: GeneAlias]
	static fetchMode = [proteins:'eager',geneAliases:'eager']
	
}
