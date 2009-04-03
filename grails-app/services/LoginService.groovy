class LoginService{
	
	def login(params){
		print params["username"]
		return new User(id:1,username:"gdocUser")
	}
	
	def logout(session){
		session.invalidate()
	}
	
}