class FindingService{
	
	def getAllFindings(){
	   def findings = []
	   findings = Finding.findAll()
	   return findings
	}
	
	def getFinding(id){
		def finding = Finding.get(id)
		return finding
	}
	
}