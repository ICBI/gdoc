

class MoleculeTests extends GroovyTestCase {
	

	void testRetrieveMolecules(){
		def molecules = Molecule.findAll()
		molecules.each{ molecule ->
			if(molecule.bindings){
				println molecule.name +":"+molecule.formula
				println "Targets ->"
				molecule.bindings.each{binding ->
					if(binding.protein)
						println binding.protein.id
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
	


}
