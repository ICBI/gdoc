class Molecule {
	static mapping = {
		table 'DRUG.MOLECULE'
		id column: 'ID'
	}
	
	String name
	String formula
	String smiles
	Long weight
	String idnumber
	Long mCluster
	Long clSize
	Float clVar
	Long available
	Long clogP
	Long donorAtoms
	Long acceptorAtoms
	Long rotatableBonds
	Long n_o
	Long condrings
	Float fosb
	Long purity
	Float tpsa
	String new_A
	Float logsw
	Long cns
	Float caco2
	Float logbb
	Float fa
	Float sw
	Float pka
	String saltdata
	Long refractivity
	String solubility
	Integer ph
	Long chiral
    String chemicalName
	Date dateCreated
	Date lastUpdated
	
	static hasMany = [bindings: MoleculeTarget, structures: Structure]
	static fetchMode = [bindings: 'eager',structures:'eager']
	
	static constraints = {
	        name(nullable: false)
			smiles(nullable: false)
			weight(nullable: true)
			formula(nullable: true)
			idnumber(nullable: true)
			mCluster(nullable: true)
			clSize(nullable: true)
			clVar(nullable: true)
			available(nullable: true)
			clogP(nullable: true)
			donorAtoms(nullable: true)
			acceptorAtoms(nullable: true)
			rotatableBonds(nullable: true)
			n_o(nullable: true)
			condrings(nullable: true)
			fosb(nullable: true)
			purity(nullable: true)
			tpsa(nullable: true)
			new_A(nullable: true)
			logsw(nullable: true)
			cns(nullable: true)
			caco2(nullable: true)
			logbb(nullable: true)
			fa(nullable: true)
			sw(nullable: true)
			pka(nullable: true)
			saltdata(nullable: true)
			refractivity(nullable: true)
			solubility(nullable: true)
			ph(nullable: true)
			chiral(nullable: true)
			chemicalName(nullable: true)
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
	
	
	
	
}