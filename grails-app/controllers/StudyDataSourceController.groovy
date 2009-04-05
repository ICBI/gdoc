class StudyDataSourceController {

	def myStudies
	def otherStudies
	def clinicalElements
	
    def index = { 
		myStudies = StudyDataSource.findByShortName("EDINBOURGH-FAKE")
		otherStudies = StudyDataSource.findAll()
		if(myStudies.metaClass.respondsTo(myStudies, "size")) {
			otherStudies.removeAll(myStudies)
		} else {
			otherStudies.remove(myStudies)
		}

	}
	
	def show = {
		def currStudy = StudyDataSource.get(params.id)
		session.study = currStudy
		StudyContext.setStudy(currStudy.schemaName)
		clinicalElements = AttributeType.findAll()
		println clinicalElements
	}
}
