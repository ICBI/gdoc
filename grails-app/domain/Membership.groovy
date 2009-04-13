class Membership {
	static mapping = {
		table 'GUIPERSIST.MEMBERSHIP'
	}
	static belongsTo = [GDOCGroup, GDOCUser]
	GDOCGroup gdocgroup
	GDOCUser user
	String role
	

}
