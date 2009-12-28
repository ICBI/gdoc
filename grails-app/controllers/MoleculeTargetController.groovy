class MoleculeTargetController {
	def clinicalService
	
	def index = {
			println params
			def protein
			def molecule
			def moleculeTarget
			if(params.target == 'EGFR'){
				protein = Protein.findByName('EGFR')
			}else if(params.target == 'p38'){
				protein = Protein.findByName('p38')
			}
			

			[bindings:protein.bindings]
		}
}