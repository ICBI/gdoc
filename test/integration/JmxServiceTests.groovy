import grails.test.*

class JmxServiceTests extends GroovyTestCase {
	
	def jmxService
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testConnectionPoolFlush() {
		jmxService.flushConnectionPool()
    }
}
