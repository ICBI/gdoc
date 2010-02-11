class StudyDataSourceService {

    boolean transactional = true

    def create(data) {
		def dataSource = new StudyDataSource(data)
		if (!dataSource.save(flush: true)) 
			println dataSource.errors
		return dataSource
    }

	def createWithContent(studyData, contentData) {
		def dataSource = create(studyData)
		contentData.each {
			def content = new DataSourceContent(it)
			dataSource.addToContent(content)
			if(!content.save(flush: true)) 
				println content.errors
			
		}
		return dataSource
	}
	
	def addPi(dataSourceId, contactData) {
		def dataSource = StudyDataSource.get(dataSourceId)
		def contact = addContact('PI', contactData)
		dataSource.addToPis(contact)
		dataSource.merge()
		return contact
	}
	
	def addPoc(dataSourceId, contactData) {
		def dataSource = StudyDataSource.get(dataSourceId)
		def contact = addContact('POINT_OF_CONTACT', contactData)
		dataSource.addToPocs(contact)
		dataSource.merge()
		return contact
	}
	
	private def addContact(contactType, contactData) {
		def contact = Contact.findByLastNameAndFirstName(contactData.lastName, contactData.firstName)
		if(!contact) {
			contact = new Contact(contactData)
			if(!contact.save(flush:true)) 
				println contact.errors
		}
		return contact
	}
}
