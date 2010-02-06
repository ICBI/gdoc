class Protein {
	
	static searchable = {
	    bindings component: true
		structures component: true
		gene component: true
		dateCreated index: 'no'
		lastUpdated index: 'no'
		labName index: 'no'
	}
	
	String name
	String labName
	static hasMany = [bindings: MoleculeTarget,structures: Structure]
	Date dateCreated
	Date lastUpdated
	static belongsTo = Gene
	Gene gene
}