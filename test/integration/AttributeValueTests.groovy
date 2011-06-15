class AttributeValueTests extends GroovyTestCase {

    void testSomething() {

    }

	void testFindAll() { 
		
		StudyContext.setStudy("EDINFAKE")
		
		def values = AttributeValue.findAll([max:10])
		values.each {
			assertNotNull it.value
			assertNotNull it.type
			assertNotNull it.type.shortName
		}
	} 

}
