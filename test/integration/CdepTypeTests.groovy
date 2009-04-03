class CdepTypeTests extends GroovyTestCase {

    void testSomething() {

    }

	void testFindAll() {
		
		def types = CdepType.findAll([max:10]);
		assert types.size >= 1
	}
}
