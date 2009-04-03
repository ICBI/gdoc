class LoginControllerTests extends GroovyTestCase {

    void testLogin() {
	
	def login_controller = new LoginController();
	login_controller.params.username = "sometestuser"
	login_controller.params.password = "somepassword"
	login_controller.login()

    }
}
