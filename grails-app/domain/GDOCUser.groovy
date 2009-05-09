class GDOCUser {
    static mapping = {
		table 'GDOCUSER'
	}
    String username
	static hasMany = [memberships:Membership,list_connections:UserListConnection,comments:Comments, analysis:SavedAnalysis]
	def groups() {
			return memberships.collect{it.gdocgroup}
	}
	def lists() {
			return list_connections.collect{it.list}
	}
	
	
    
}