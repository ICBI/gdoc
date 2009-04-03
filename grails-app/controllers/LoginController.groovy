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
						def user = new User()
						redirect(controller:'home')
					}
					else {
					   	def user = loginService.login(params)//User.findByUserIdAndPassword(params.userId, params.password)
						if (user) {
							session.userId = user.username
							redirect(controller:'user')
						}
						else {
							flash['message'] = 'Please enter a valid user ID and password'
						}
					}
				}
		  
		}
		
		def logout = {
			loginService.logout(session)
			redirect(controller:'home')
		}
	
	
}
