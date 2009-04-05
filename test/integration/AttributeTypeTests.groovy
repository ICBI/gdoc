class AttributeTypeTests extends GroovyTestCase {

    void testSomething() {

    }

	void testFindAll() {
		
		def types = AttributeType.findAll([max:10]);
		assert types.size >= 1
	}
}
