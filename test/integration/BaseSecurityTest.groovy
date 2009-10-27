import org.springframework.mock.jndi.SimpleNamingContextBuilder

class BaseSecurityTest extends GroovyTestCase {
	
	def securityService
	def jdbcTemplate
	def testDataSource
	
	void setUp() {
		SimpleNamingContextBuilder builder =
			SimpleNamingContextBuilder.emptyActivatedContextBuilder()

		builder.bind("java:/gdoc", testDataSource);
		securityService.jdbcTemplate = jdbcTemplate
	}
}