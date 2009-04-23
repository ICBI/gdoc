class StudyDataSourceController {

	def myStudies
	def otherStudies
	def clinicalElements
	
    def index = { 
		myStudies = StudyDataSource.findByShortName("EDINBURGH")
		otherStudies = StudyDataSource.findAll()
		println myStudies
		if(myStudies.metaClass.respondsTo(myStudies, "size")) {
			otherStudies.removeAll(myStudies)
		} else {
			otherStudies.remove(myStudies)
		}

	}
	
	def show = {
		def currStudy = StudyDataSource.get(params.id)
		session.study = currStudy
		clinicalElements = AttributeType.findAll()
		println clinicalElements
	}
}
