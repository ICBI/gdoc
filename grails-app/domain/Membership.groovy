class Membership {
	static mapping = {
		table 'MEMBERSHIP'
	}
	static belongsTo = [GDOCGroup, GDOCUser]
	GDOCGroup gdocgroup
	GDOCUser user
	String role
	

}
