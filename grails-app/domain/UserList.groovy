class UserList implements Taggable{
	static mapping = {
		table 'USER_LIST'
		studies joinTable: [name: 'LIST_STUDY', column: 'STUDY_ID', key: 'LIST_ID'] 
	}

	String name
	GDOCUser author
	Date dateCreated
	Date lastUpdated
	
	static hasMany = [listItems:UserListItem,listComments:Comments, studies:StudyDataSource]
	static fetchMode = [listItems: 'eager',tags:'eager', studies: 'eager']
	static constraints = {
		name(blank:false)
		author(blank:false)
	}
	
	def studyNames = {
		return studies.collect {it.shortName}.join(", ")
	}

}
