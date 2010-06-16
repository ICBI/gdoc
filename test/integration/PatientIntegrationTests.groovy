class PatientIntegrationTests extends GroovyTestCase {


	void setUp() {
	}
	
	void testFindAll() { 
		
		StudyContext.setStudy("EDIN")
		
		def patients = Patient.findAll([max:10])
		assert patients.size() > 0
		println patients[0]
		println patients[0].values.size()
		patients[0].values.each() {
			assertNotNull it.value
		}
	} 
	

	
}