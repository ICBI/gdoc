class AttributeTypeTests extends GroovyTestCase {

    void testSomething() {

    }

	void testFindAll() {
		StudyContext.setStudy('EDIN')
		def types = AttributeType.findAll([max:10]);
		assert types.size >= 1
	}
}
