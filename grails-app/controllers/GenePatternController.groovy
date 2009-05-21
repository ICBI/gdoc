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
			genePatternService.submitJob(session.userId, cmd)
			redirect(controller:'notification')
		}
	}
}
