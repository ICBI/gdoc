class GDOCUser {
    static mapping = {
		table 'GUIPERSIST.GDOCUSER'
	}
    String username
	static hasMany = [memberships:Membership,list_connections:UserListConnection,comments:Comments]
	def groups() {
			return memberships.collect{it.gdocgroup}
	}
	def lists() {
			return list_connections.collect{it.list}
	}
	
	
    
}