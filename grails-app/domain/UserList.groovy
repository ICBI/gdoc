class UserList implements Taggable{
	static mapping = {
		table 'GUIPERSIST.USER_LIST'
	}
	//, list_comments:Comments
	String name
	GDOCUser author
	static hasMany = [list_items:UserListItem, list_connections:UserListConnection]
	static constraints = {
		name(blank:false)
		author(blank:false)
	}

}
