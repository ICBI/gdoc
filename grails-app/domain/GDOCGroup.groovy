class GDOCGroup {
	static mapping = {
		table 'GDOCGROUP'
	}
	String name;
	String study;
	static hasMany = [memberships:Membership]
}
