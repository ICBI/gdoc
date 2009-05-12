class Comments {
	static mapping = {
		table 'COMMENTS'
	}
	static belongsTo = [GDOCUser, List]
	GDOCUser user
	List list
	String commentText

}
