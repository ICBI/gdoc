class MoleculeTarget{
	static mapping = {
		table 'DRUG.MOLECULE_TARGET'
	}
	String bindingData
	static belongsTo = [Molecule, Protein]
	
	static hasMany = [structures: Structure]
	Molecule molecule
	Protein protein
	Date dateCreated
	Date lastUpdated
	static constraints = {
		molecule(nullable:false)
		protein(nullable:false)
	}
	
	
}