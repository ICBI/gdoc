class MoleculeTargetController {
	def drugDiscoveryService
	
	def index = {
			println params
			def protein
			def molecule
			def moleculeTarget
			if(params.target){
				protein = Protein.findByNameIlike(params.target)
			}
			if(protein)
				[bindings:protein.bindings]
				
		}
		
	def display = {
		try{
			def file = new File(grailsApplication.config.structuresPath + params.inputFile)
			byte[] fileBytes = file.readBytes()
				response.outputStream << fileBytes
		}catch(java.io.FileNotFoundException fnf){
			println fnf.toString()
			render "File ($params.inputFile) was not found...is the file name correct?"
		}
	}
}