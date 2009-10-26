class Membership {
	static mapping = {
			table 'CSM_USER_GROUP_ROLE_PG'
			version false
			id column:'USER_GROUP_ROLE_PG_ID'
			
	}
	//static belongsTo = [GDOCGroup, GDOCUser]
	static belongsTo = [GDOCUser]
	Role role
	ProtectionGroup protectionGroup
	GDOCUser user
	
	

}
