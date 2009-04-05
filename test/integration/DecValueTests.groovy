class DecValueTests extends GroovyTestCase {

    void testSomething() {

    }

	void testFindAll() { 
		
		StudyContext.setStudy("EDINFAKE")
		
		def values = DecValue.findAll([max:10])
		values.each {
			assertNotNull it.value
			assertNotNull it.type
			assertNotNull it.type.shortName
		}
	} 

}
