class GDOCGroupTests extends GroovyTestCase {

    void testSomething() {
		def gd = new GDOCGroup(name:'egroup',study:'EDIN').save();
		assertTrue GDOCGroup.findByName('egroup').name == gd.name;
    }
}
