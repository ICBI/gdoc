class UserListConnection {
	static mapping = {
		table 'USER_LIST_CONNECTION'
	}
	static belongsTo = [GDOCUser, UserList]
	GDOCUser user
	UserList list
	Integer rating

}
