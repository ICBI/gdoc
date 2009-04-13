class GDOCGroup {
	static mapping = {
		table 'GUIPERSIST.GDOCGROUP'
	}
	String name;
	String study;
	static hasMany = [memberships:Membership]
}
