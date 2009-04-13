class Comments {
	static mapping = {
		table 'GUIPERSIST.COMMENTS'
	}
	static belongsTo = [GDOCUser, List]
	GDOCUser user
	List list
	String comment_text

}
