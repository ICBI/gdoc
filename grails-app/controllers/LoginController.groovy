import LoginException

class LoginController {
	def loginService
 	def login = {
	  LoginCommand cmd -> if(cmd.hasErrors()) {
						flash['message'] = "login failed."
		                redirect(controller:'home',action:'index')
		         }
		         else {
		            if (request.method == "GET") {
						session.userId = null
						def user = new GDOCUser()
						redirect(controller:'home')
					}
					else {
						try{
							def user = loginService.login(params)
							if (user) {
								session.userId = user.login_name
								//println (user.login_name)
								redirect(controller:'studyDataSource')
							}
							else {
								flash['message'] = 'Please enter a valid user ID and password'
								redirect(controller:'home')
							}
						}catch(LoginException le){
							println "login invalid in controller"
							flash['message'] = 'Please enter a valid user ID and password'
							redirect(controller:'home')
						}
					}
				}
		}
		
		def logout = {
			println params
			loginService.logout(session)
			flash.message = "user logged out"
			redirect(controller:'home')
		}
	
	
}
