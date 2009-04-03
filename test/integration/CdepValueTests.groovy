class CdepValueTests extends GroovyTestCase {

    void testSomething() {

    }

	void testFindAll() { 
		
		StudyContext.setStudy("EDIN")
		
		def values = CdepValue.findAll([max:10])
		values.each {
			assertNotNull it.value
			assertNotNull it.type
			assertNotNull it.type.shortName
		}
	} 

}
