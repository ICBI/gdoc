abstract class BaseIntegrationTest extends GroovyTestCase {
	def sessionFactory
	def transaction

    protected void setUp() {
        super.setUp()
		def session = sessionFactory.getCurrentSession()
		transaction = session.beginTransaction()
    }

    protected void tearDown() {
        super.tearDown()
		transaction.rollback()
    }

	public void saveObject(domainObject) {
		if(!domainObject.save())
			println domainObject.errors
	}
}