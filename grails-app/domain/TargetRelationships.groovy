class TargetRelationships {
	static mapping = {
		table 'DRUG.TARGET_RELATIONSHIPS'
		version false
		id column:'ID'
	}
	
	static searchable = {
		alias "targetRelationships"
	    moleculeTarget component: true
	}
	
	String symbol
	String name
	Long isoform
	
	static belongsTo = [MoleculeTarget, Gene, Protein]
	MoleculeTarget moleculeTarget
	Gene gene
	Protein protein

	
}
