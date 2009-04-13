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
						def user = loginService.login(params)//User.findByUserIdAndPassword(params.userId, params.password)
						if (user) {
							session.userId = user.username
							redirect(controller:'studyDataSource')
						}
						else {
							flash['message'] = 'Please enter a valid user ID and password'
						}
					}
				}
		}
		
		def logout = {
			println params
			loginService.logout(session)
			flash.message = "user loggd out"
			redirect(controller:'home')
		}
	
	
}
