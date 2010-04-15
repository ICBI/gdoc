import LoginException

class LoginController {
	def securityService
 	def login = {
	  LoginCommand cmd -> if(cmd.hasErrors()) {
						flash['cmd'] = cmd
						flash['message'] = "login failed."
		                redirect(controller:'home',action:'index')
		         }
		         else {
					flash['cmd'] = cmd
		            if (request.method == "GET") {
						session.userId = null
						def user = new GDOCUser()
						redirect(controller:'home')
					}
					else {
						try{
							def user = securityService.login(params)
							if (user) {
								session.userId = user.loginName
								//println (user.loginName)
								redirect(controller:'workflows')
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
			securityService.logout(session)
			flash.message = "user logged out"
			println "user logged out"
			redirect(controller:'home',action:'index')
		}
	
	
}
