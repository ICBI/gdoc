class GDOCGroup {
	static mapping = {
			table 'CSM_GROUP'
			version false
			id column:'GROUP_ID'
			groupName column: 'group_name'
			groupDesc column: 'group_desc'
	}
	String groupName;
	String groupDesc;
	static hasMany = [memberships:Membership]
}
