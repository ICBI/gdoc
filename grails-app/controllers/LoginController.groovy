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
								session.userId = user.username
								if(cmd.desiredPage){
									log.debug "user wished to go to $cmd.desiredPage view"
									redirect(controller:'workflows',params:[desiredPage:cmd.desiredPage])
								}
								else{
									redirect(controller:'workflows')
								}
								
							}
							else {
								flash['message'] = 'Please enter a valid user ID and password'
								redirect(controller:'home')
							}
						}catch(LoginException le){
							log.debug "login invalid in controller"
							flash['message'] = 'Please enter a valid user ID and password'
							redirect(controller:'home')
						}
					}
				}
		}
		
		def logout = {
			log.debug params
			securityService.logout(session)
			flash.message = "user logged out"
			log.debug "user logged out"
			redirect(controller:'home',action:'index')
		}
	
	
}
