

class FindingTests extends GroovyTestCase {
	

	void testRetrieveFindings(){
		def findings = Finding.findAll()
		findings.each{ finding ->
			println "title" +":"+finding.title
			println "Principal Evidence -> $finding.principalEvidence"
			println "Supporting Evidence ->"
			def evidence = []
			evidence = finding.supportingEvidence
			evidence.each{e ->
				if(e.userList)
					println "user list: $e.userList.name"
				if(e.savedAnalysis)
					println "savedAnalysis: $e.savedAnalysis"
				if(e.relatedFinding)
					println "relatedFinding: $e.relatedFinding"
			}
		}
		
	}
	


}
