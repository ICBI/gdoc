

class MoleculeTests extends GroovyTestCase {
	

	void testRetrieveMolecules(){
		def molecules = Molecule.findAll()
		molecules.each{ molecule ->
			println molecule.name +":"+molecule.formula
			println "Targets ->"
			molecule.bindings.each{binding ->
				println binding.bindingData
				if(binding.protein)
					println binding.protein.name
					//println binding.protein.gene
			}
			println "Structure(s) ->"
			molecule.structures.each{structure ->
				println structure.structureFile.relativePath
			}
			println "-----------------------------------"
		}
	}
	


}
