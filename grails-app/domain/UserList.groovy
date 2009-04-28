class UserList implements Taggable{
	static mapping = {
		table 'USER_LIST'
	}
	String name
	GDOCUser author
	static hasMany = [list_items:UserListItem, list_connections:UserListConnection, list_comments:Comments]
	static constraints = {
		name(blank:false)
		author(blank:false)
	}

}
