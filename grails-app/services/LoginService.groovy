class LoginService{
	
	def login(params){
		print params["username"]
		if(params["username"] == "gdocuser" && params["password"] == "gdocpass") {
			return new GDOCUser(id:1,username:"gdocuser")
		} else {
			return null
		}
		
	}
	
	def logout(session){
		session.invalidate()
	}
	
}