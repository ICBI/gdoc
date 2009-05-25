class LoginControllerTests extends GroovyTestCase {
	def securityService

    void testLogin() {
	
	def login_controller = new LoginController();
	login_controller.securityService = securityService
	login_controller.params.login_name = "gdocUser"
	login_controller.params.password = "gdocLCCC"
	login_controller.login()

    }
}
