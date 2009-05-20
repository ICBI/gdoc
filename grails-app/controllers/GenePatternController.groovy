class GenePatternController {

	def genePatternService
	
    def index = { }

	def submit = { GenePatternCommand cmd ->
		genePatternService.submitJob(session.userId, cmd)
		redirect(controller:'notification')
	}
}
