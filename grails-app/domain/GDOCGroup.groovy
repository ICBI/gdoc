class GDOCGroup {
	static mapping = {
			table 'CSM_GROUP'
			version false
			id column:'GROUP_ID'
	}
	String group_name;
	String group_desc;
	static hasMany = [memberships:Membership]
}
