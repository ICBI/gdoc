import org.grails.taggable.*

class UserList implements Taggable{
	static mapping = {
		table 'USER_LIST'
		id column: 'ID'
		studies joinTable: [name: 'LIST_STUDY', column: 'STUDY_ID', key: 'LIST_ID'] 
		cache true
	}

	String name 
	Date dateCreated
	Date lastUpdated
	
	static searchable = {
	    listItems component: true
	}
	static belongsTo = [author:GDOCUser]
	static hasMany = [listItems:UserListItem,listComments:Comments, studies:StudyDataSource, evidence:Evidence]
	static fetchMode = [listItems: 'eager',tags:'eager', studies: 'eager']
	static constraints = {
		name(blank:false)
		author(blank:false)
	}
	
	def studyNames = {
		return studies.collect {it.shortName}
	}
	
	def schemaNames = {
		return studies.collect {it.schemaName}
	}
	
}
