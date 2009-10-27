class Membership {
	static mapping = {
			table 'CSM_USER_GROUP_ROLE_PG'
			version false
			id column:'USER_GROUP_ROLE_PG_ID'
			collaborationGroup column: 'PROTECTION_GROUP_ID'
	}
	static belongsTo = [GDOCUser]
	Role role
	CollaborationGroup collaborationGroup
	GDOCUser user
	
	

}
