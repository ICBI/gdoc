class Molecule {
	static mapping = {
		table 'DRUG.MOLECULE'
	}
	
	static constraints = {
		name(blank:false)
		formula(blank:false)
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