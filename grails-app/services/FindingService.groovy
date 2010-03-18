class FindingService{
	
	def getAllFindings(){
	   def findings = []
	   findings = Finding.findAll()
	   findings = findings.sort { one, two ->
			def dateOne = one.dateCreated
			def dateTwo = two.dateCreated
			return dateTwo.compareTo(dateOne)
		}
	   return findings
	}
	
	def getFinding(id){
		def finding = Finding.get(id)
		return finding
	}
	
}