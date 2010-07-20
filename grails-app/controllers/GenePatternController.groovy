@Mixin(ControllerMixin)
class GenePatternController {

	def genePatternService
	def userListService
	
    def index = {
		if(session.study) {
			session.files = MicroarrayFile.findAllByNameLike('%.Rda')
		}
		loadPatientLists()
		loadGeneLists()
		[diseases:getDiseases()]
		
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
