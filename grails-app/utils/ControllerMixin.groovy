class ControllerMixin {
    static getDiseases(self) {
		def diseases = self.session.myStudies.collect{it.cancerSite}.unique().sort()
		diseases.remove("N/A")
		return diseases
    }

	static loadPatientLists(self) {
		if(self.session.study){
			StudyContext.setStudy(self.session.study.schemaName)
			def lists = self.userListService.getAllLists(self.session.userId, self.session.sharedListIds)
			def patientLists = lists.findAll { item ->
				(item.tags.contains("patient") && item.schemaNames().contains(StudyContext.getStudy()))
			}

			self.session.patientLists = patientLists.sort {it.name}
		}
	}
	
	static loadReporterLists(self) {
		if(self.session.study) {
			def reporterLists = []
			def lists = self.userListService.getAllLists(self.session.userId, self.session.sharedListIds)
			lists.each { item ->
				if(item.tags.contains("reporter"))
					reporterLists << item
			}
			self.session.reporterLists = reporterLists.sort {it.name}
		}
	}
	
	static loadGeneLists(self) {
		def lists = self.userListService.getAllLists(self.session.userId,self.session.sharedListIds)
		def geneLists = lists.findAll { item ->
			item.tags.contains("gene")
		}
		self.session.geneLists = geneLists.sort {it.name}
	}
	
	static loadCurrentStudy(self) {
		def currStudy = StudyDataSource.findBySchemaName(StudyContext.getStudy())
		if(!self.session.study || (currStudy.schemaName != self.session.study.schemaName)){
			self.session.study = currStudy
			self.session.dataTypes = AttributeType.findAll().sort { it.longName }
			loadPatientLists(self)
			loadReporterLists(self)
			loadGeneLists(self)
			self.session.endpoints = KmAttribute.findAll()
			self.session.files = self.htDataService.getHTDataMap()
			self.session.dataSetType = self.session.files.keySet()
		}
			
	}
}
