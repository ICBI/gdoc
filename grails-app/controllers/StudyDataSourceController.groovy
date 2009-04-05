class StudyDataSourceController {

	def myStudies
	def otherStudies
	
    def index = { 
		myStudies = StudyDataSource.findByShortName("EDINBOURGH")
		otherStudies = StudyDataSource.findAll()
		if(myStudies.metaClass.respondsTo(myStudies, "size")) {
			otherStudies.removeAll(myStudies)
		} else {
			otherStudies.remove(myStudies)
		}

	}
}
