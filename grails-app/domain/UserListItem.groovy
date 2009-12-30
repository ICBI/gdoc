
class UserListItem {
	
	static mapping = {
		table 'USER_LIST_ITEM'
	}

	static belongsTo = UserList
	UserList list
	String value

}
