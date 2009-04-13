class LoginService{
	
	def login(params){
		print params["username"]
		return new GDOCUser(id:1,username:"gdocUser")
	}
	
	def logout(session){
		session.invalidate()
	}
	
}