
class UserListItem {
	static mapping = {
		table 'GUIPERSIST.USER_LIST_ITEM'
	}
	static belongsTo = UserList
	UserList list
	String value

}
