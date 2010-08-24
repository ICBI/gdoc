class Molecule {
	static mapping = {
		table 'DRUG.MOLECULE'
		id column: 'ID'
	}
	
	String name
	String formula
	String smiles
	Double weight
	String idnumber
	Long mCluster
	Long clSize
	Float clVar
	Long available
	Float clogP
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
	CollaborationGroup protectionGroup
	
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
		alias "molecule"
	    bindings component: true
		structures component: true
		weight index: "not_analyzed", format: "000000000"
		protectionGroup component: [prefix:'collaborationGroup',maxDepth:2]
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
		mCluster index: 'no'
		clSize index: 'no'
		clVar index: 'no'
		available index: 'no'
		n_o index: 'no'
		condrings index: 'no'
		fosb index: 'no'
		purity index: 'no'
		tpsa index: 'no'
		new_A index: 'no'
		logsw index: 'no'
		cns index: 'no'
		caco2 index: 'no'
		logbb index: 'no'
		fa index: 'no'
		sw index: 'no'
		pka index: 'no'
		saltdata index: 'no'
	}
	
	
	
	
}