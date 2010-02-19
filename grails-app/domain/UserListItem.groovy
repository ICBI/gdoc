
class UserListItem {
	
	static mapping = {
		table 'USER_LIST_ITEM'
	}
	
	static searchable = {
	    list reference: true
	}

	static belongsTo = UserList
	UserList list
	String value

}
