class GenePatternController {

	def genePatternService
	
    def index = {
		if(params.id) {
			def currStudy = StudyDataSource.get(params.id)
			session.study = currStudy
			StudyContext.setStudy(session.study.schemaName)
		}
		def lists = GDOCUser.findByLoginName(session.userId).lists()
		def patientLists = lists.findAll { item ->
			(item.tags.contains("patient") && item.tags.contains(StudyContext.getStudy()))
		}
		session.lists = patientLists
	}

	def submit = { GenePatternCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index')
		} else {
			def jobId = genePatternService.submitJob(session.userId, cmd)
			if(!session.genePatternJobs) {
				session.genePatternJobs = []
			} 
			session.genePatternJobs << [jobId: jobId, complete: false, dateCreated: new Date()]
			redirect(controller:'notification')
		}
	}
}
