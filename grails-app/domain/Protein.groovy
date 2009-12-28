class Protein {
	
	String name
	String labName
	static hasMany = [bindings: MoleculeTarget,structures: Structure]
	Date dateCreated
	Date lastUpdated
	static belongsTo = GeneAlias
	GeneAlias gene
}