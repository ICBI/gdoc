class Membership {
	static mapping = {
			table 'CSM_USER_GROUP'
			version false
			id column:'USER_GROUP_ID'
	}
	static belongsTo = [GDOCGroup, GDOCUser]
	GDOCGroup group
	GDOCUser user
	//String role
	

}
