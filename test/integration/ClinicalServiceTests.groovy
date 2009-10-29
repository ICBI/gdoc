class ClinicalServiceTests extends GroovyTestCase {

	def clinicalService
	
    void testSomething() {

    }

	void testQueryByCriteria() {
		StudyContext.setStudy("EDINFAKE")
		def patients = clinicalService.queryByCriteria(['DR':'YES', 'VITAL_STATUS':'ALIVE', 'LR':'YES'], [])
		patients.each { patient -> 
			assertTrue (patient.clinicalData["DR"] == "YES")
			assertTrue (patient.clinicalData["LR"] == "YES")
			assertTrue (patient.clinicalData["VITAL_STATUS"] == "ALIVE")
		}

	}
	
	void testQueryByMultipleCriteria() {
		StudyContext.setStudy("EDINFAKE")
		
		def patients = clinicalService.queryByCriteria(['DR':'YES', 'VITAL_STATUS':'ALIVE'], [])
		patients.each { patient -> 
			assertTrue (patient.clinicalData["DR"] == "YES")
			assertTrue (patient.clinicalData["VITAL_STATUS"] == "ALIVE")
		}
	}
	
	void testNoResults() {
		StudyContext.setStudy("EDINFAKE")
		
		def patients = clinicalService.queryByCriteria(['no type':'test value'], [])
		assertTrue patients.size() == 0	
	}
}
