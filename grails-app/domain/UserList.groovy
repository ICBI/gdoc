class UserList implements Taggable{
	static mapping = {
		table 'USER_LIST'
	}

	String name
	GDOCUser author
	Date dateCreated
	Date lastUpdated
	static hasMany = [listItems:UserListItem,listComments:Comments]
	static fetchMode = [listItems: 'lazy',tags:'eager']
	static constraints = {
		name(blank:false)
		author(blank:false)
	}
	

}
