class MoleculeTargetController {
	def drugDiscoveryService
	
	def index = {
			println params
			def protein
			def molecule
			def moleculeTarget
			if(params.target){
				protein = Protein.findByName(params.target)
			}
			if(protein)
				[bindings:protein.bindings]
		}
}