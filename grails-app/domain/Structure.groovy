class Structure{
	static mapping = {
		table 'DRUG.STRUCTURE'
	}
	static belongsTo = [Molecule, Protein, MoleculeTarget]
	String structureDb
	Molecule molecule
	Protein protein
	MoleculeTarget moleculeTarget
	StructureFile structureFile
	Date dateCreated
	Date lastUpdated
	static constraints = {
		structureFile(nullable:false)
	}
	
}