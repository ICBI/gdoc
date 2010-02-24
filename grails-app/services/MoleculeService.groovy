import groovy.inspect.Inspector

class MoleculeService {

	boolean transactional = true

	def createMolecule(data) {
		println Molecule.count()
		def molecule = new Molecule(data)
		
		if (!molecule.save(flush: true)) {
			println molecule.errors
		}
		else {
			createStructure(molecule)
		}
		return molecule
	}
	
	def createStructure(molecule) {
		def imgPath
		def structureFile
		def structure
		println StructureFile.count()
		if(molecule.idnumber){
			imgPath = molecule.idnumber + "_" + molecule.id + ".png"
		}else{
			imgPath = molecule.name + "_" + molecule.id + ".png"
		}
		 structureFile = new StructureFile(name:molecule.name,relativePath:imgPath)
		if (!structureFile.save(flush: true)) {
			println structureFile.errors
		}else{
			println Structure.count()
			structure = new Structure(structureFile:structureFile,molecule:molecule)
			if (!structure.save(flush: true)) {
				println structure.errors
			}
		}
		return structure
	}
	
}