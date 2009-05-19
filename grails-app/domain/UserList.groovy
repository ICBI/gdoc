class UserList implements Taggable{
	static mapping = {
		table 'USER_LIST'
	}
	String name
	GDOCUser author
	static hasMany = [listItems:UserListItem,listConnections:UserListConnection, listComments:Comments]
	static constraints = {
		name(blank:false)
		author(blank:false)
	}
	
	public void setGroups(List groups) {
		
		this.@analysisData = data as AnalysisJSON
	}
	
	public List getGroups() {
		return this.@groups
	}

}
