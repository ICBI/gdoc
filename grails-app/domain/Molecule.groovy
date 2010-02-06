class Molecule {
	static mapping = {
		table 'DRUG.MOLECULE'
	}
	
	static constraints = {
		name(blank:false)
		formula(blank:false)
	}
	
	static searchable = {
		name boost:2
	    bindings component: true
		structures component: true
		dateCreated index: 'no'
		lastUpdated index: 'no'
		refractivity index: 'no'
		solubility index: 'no'
		ph index: 'no'
		donorAtoms index: 'no'
		acceptorAtoms index: 'no'
		clogP index: 'no'
		rotatableBonds index: 'no'
		chiral index: 'no'
		chemicalName index: 'no'
	}
	
	String name
	String formula
	Long weight
	Float refractivity
	String solubility
	static hasMany = [bindings: MoleculeTarget, structures: Structure]
	Integer ph
	Long donorAtoms
	Long acceptorAtoms
	Long clogP
	Long rotatableBonds
	Long chiral
    String chemicalName
	Date dateCreated
	Date lastUpdated
	
	
}