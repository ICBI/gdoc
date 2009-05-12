class GDOCUser {
    static mapping = {
		table 'CSM_USER'
		version false
		id column:'user_id'
		column name: "login_name", unique: true
		 
	}
	
	String login_name
	String first_name
	String last_name
	String password
	
	static hasMany = [groups:Membership,list_connections:UserListConnection,comments:Comments]
	def groups() {
			return memberships.collect{it.gdocgroup}
	}
	def lists() {
			return list_connections.collect{it.list}
	}
	
	
    
}