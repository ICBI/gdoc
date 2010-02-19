class FindingController {
	def findingService
	
	def index = {
		def findings = findingService.getAllFindings()
		[findings:findings]
	}
	
	def show = {
		def finding
		if(params.id){
			finding = findingService.getFinding(params.id)
		}
		[finding:finding]
	}
	
	
}