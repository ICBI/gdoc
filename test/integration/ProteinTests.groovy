

class ProteinTests extends GroovyTestCase {
	

	void testRetrieveProtein(){
		def protein = Protein.findByName('EGFR')
		println protein.gene
	}


}
