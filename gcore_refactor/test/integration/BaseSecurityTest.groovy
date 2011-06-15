import org.springframework.mock.jndi.SimpleNamingContextBuilder

class BaseSecurityTest extends BaseIntegrationTest {
	
	def securityService
	def jdbcTemplate
	def testDataSource
	
	void setUp() {
		super.setUp()
		SimpleNamingContextBuilder builder =
			SimpleNamingContextBuilder.emptyActivatedContextBuilder()

		builder.bind("java:/gdoc", testDataSource);
		securityService.jdbcTemplate = jdbcTemplate
	}
}