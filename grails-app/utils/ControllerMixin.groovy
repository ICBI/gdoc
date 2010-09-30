class ControllerMixin {
	
	static isAccessible(self, id){
		def accessible = false
		def analysisId = new Long(id)
		def allAnalysisIds = self.savedAnalysisService.getAllAnalysesIds(self.session.sharedAnalysisIds,self.session.userId)
		if (allAnalysisIds && allAnalysisIds.contains(analysisId))
			accessible = true
		return accessible
	}
	
	static isListAccessible(self, id){
		def accessible = false
		def listId = new Long(id)
		def allListIds = self.userListService.getAllListIds(self.session.sharedListIds,self.session.userId)
		if (allListIds && allListIds.contains(listId))
			accessible = true
		return accessible
	}
	
	static isAnalysisAuthor(self, id){
		def isAuthor = false
		self.log.debug "LOOKING UP author of analysis $id"
		def item = SavedAnalysis.get(id)
		if(self.session.userId == item.author.loginName){
			isAuthor = true
		}
		return isAuthor
	}
	
	static isListAuthor(self, id){
		def isAuthor = false
		self.log.debug "LOOKING UP author of list $id"
		def item = UserList.get(id)
		if(self.session.userId == item.author.loginName){
			isAuthor = true
		}
		return isAuthor
	}
	
    static getDiseases(self) {
		def diseases = self.session.myStudies.collect{it.cancerSite}.unique().sort()
		diseases.remove("N/A")
		return diseases
    }

	static loadPatientLists(self) {
		if(self.session.study){
			StudyContext.setStudy(self.session.study.schemaName)
			def lists = []
			lists = self.userListService.getListsByTagAndStudy(Constants.PATIENT_LIST,self.session.study,self.session.sharedListIds, self.session.userId)
			self.session.patientLists = lists
		}
	}
	
	static loadReporterLists(self) {
		if(self.session.study) {
			def reporterLists = []
			reporterLists = self.userListService.getListsByTag(Constants.REPORTER_LIST,self.session.sharedListIds,self.session.userId)
			self.session.reporterLists = reporterLists
		}
	}
	
	static loadGeneLists(self) {
		if(self.session.study) {
			def geneLists = []
			geneLists = self.userListService.getListsByTag(Constants.GENE_LIST,self.session.sharedListIds,self.session.userId)
			self.session.geneLists = geneLists
		}
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
