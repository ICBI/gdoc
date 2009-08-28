class BiospecimenTests extends GroovyTestCase {

    void testBiospecimenValues() {
		StudyContext.setStudy("LOI")
		def specimens = Biospecimen.findAll()
		specimens.each { specimen ->
			assertNotNull(specimen.biospecimenData)
			specimen.biospecimenData.each {
				println it
			}
		}
		
    }
}
