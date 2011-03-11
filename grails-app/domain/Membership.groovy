class Membership {
	static mapping = {
			table 'MEMBERSHIP'
			version false
			id column:'MEMBERSHIP_ID'
			collaborationGroup column: 'COLLABORATION_GROUP_ID'
	}
	static belongsTo = [GDOCUser]
	Role role
	CollaborationGroup collaborationGroup
	GDOCUser user
	
	

}
