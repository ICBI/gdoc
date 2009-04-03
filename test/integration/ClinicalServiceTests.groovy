class ClinicalServiceTests extends GroovyTestCase {

	def clinicalService
	
    void testSomething() {

    }

	void testQueryByCriteria() {
		def patients = clinicalService.queryByCriteria(['DR':'YES'])
		patients.each { patient -> 
			assertTrue (patient.clinicalData["Distal Recurrence"] == "YES")
		}

	}
	
	void testQueryByMultipleCriteria() {
		def patients = clinicalService.queryByCriteria(['DR':'YES', 'VITAL_STATUS':'ALIVE'])
		patients.each { patient -> 
			assertTrue (patient.clinicalData["Distal Recurrence"] == "YES")
			assertTrue (patient.clinicalData["Vital Status"] == "ALIVE")
		}
	}
	
	void testNoResults() {
		def patients = clinicalService.queryByCriteria(['no type':'test value'])
		assertTrue patients.size() == 0	
	}
}
