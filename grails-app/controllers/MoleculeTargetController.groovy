class MoleculeTargetController {
	def clinicalService
	
	def index = {
		def moleculePath = "applets/compound.sdf"
		def moleculeTargetPath = "applets/1A47.pdb"
		[moleculePath: moleculePath,moleculeTargetPath:moleculeTargetPath]
	}
}