class LoginControllerTests extends GroovyTestCase {
	def loginService

    void testLogin() {
	
	def login_controller = new LoginController();
	login_controller.loginService = loginService
	login_controller.params.username = "sometestuser"
	login_controller.params.password = "somepassword"
	login_controller.login()

    }
}
