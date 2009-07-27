class SampleController {
	def caTissueService
	def summary
    def index = { 
		summary = caTissueService.sampleSummary()
		println summary
	}
}
