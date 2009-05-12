class LoginControllerTests extends GroovyTestCase {
	def loginService

    void testLogin() {
	
	def login_controller = new LoginController();
	login_controller.loginService = loginService
	login_controller.params.login_name = "kmr75"
	login_controller.params.password = "Positano2006!"
	login_controller.login()

    }
}
