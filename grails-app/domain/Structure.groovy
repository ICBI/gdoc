class Structure{
	static mapping = {
		table 'DRUG.STRUCTURE'
	}
	
	static searchable = {
	    molecule component: true
		protein component: true
		moleculeTarget component: true
		structureFile component: true
		dateCreated index: 'no'
		lastUpdated index: 'no'
		structureDb index: 'no'
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