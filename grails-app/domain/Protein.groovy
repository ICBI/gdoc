class Protein {
	static mapping = {
		table 'COMMON.PROTEIN'
		version false
		id column:'PROTEIN_ID'
	}
	
	static searchable = {
		alias "protein"
	    bindings component: true
		structures component: true
		gene component: true
	}
	
	String name
	static belongsTo = Gene
	Gene gene
	static hasMany = [bindings: MoleculeTarget,structures: Structure]
	String accession
	String accessionVer
	//String labName
	//Date dateCreated
	//Date lastUpdated
	
}