class StudyDataSourceController {

	def myStudies
	def myCollaborationGroups
	def otherStudies
	def clinicalElements
	def securityService
	
    def index = { 
		def studyNames = securityService.getSharedItemIds(session.userId, StudyDataSource.class.name)
		println studyNames
		myStudies = []
		myCollaborationGroups = securityService.getCollaborationGroups(session.userId)
		studyNames.each{
			def foundStudy = StudyDataSource.findByShortName(it)
			if(foundStudy){
				myStudies << foundStudy
			}
		}
		otherStudies = StudyDataSource.findAll()
		println myStudies
		if(myStudies.metaClass.respondsTo(myStudies, "size")) {
			otherStudies.removeAll(myStudies)
		} else {
			otherStudies.remove(myStudies)
		}
		def sharedLists = []
		sharedLists = securityService.getSharedItemIds(session.userId, UserList.class.name)
		session.sharedListIds = sharedLists
		session.myStudies = myStudies
		session.myCollaborationGroups = myCollaborationGroups
	}
	
	def show = {
		def currStudy = StudyDataSource.get(params.id)
		session.study = currStudy
		clinicalElements = AttributeType.findAll()
		println clinicalElements
	}
}
