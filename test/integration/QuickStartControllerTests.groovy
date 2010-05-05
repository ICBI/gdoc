class QuickStartControllerTests extends grails.test.ControllerUnitTestCase {
	def clinicalService
	def session
	def params
	
	void setUp(){
		session = [ : ]
		QuickStartController.metaClass.getSession = { -> session }
		params = [ : ]
		QuickStartController.metaClass.getParams = { -> params }
		
	}
	
    void testQueryForCounts() {
		StudyContext.setStudy("EDIN")
		def patients = clinicalService.getPatientIdsForCriteria(['BREASTCANCER_DEATH':'YES', 'RELAPSE_CODE':1, 'DR':'YES'], [])
		println patients.size()
		
		def patients2 = []
	 	patients2 = clinicalService.getPatientIdsForCriteria([], [])
		println patients2.size()
    }

	void testQueryForRelapse() {
		def currStudy = StudyDataSource.findByShortName("CLARKE-LIU")
		def currStudy2 = StudyDataSource.findByShortName("CRC_PILOT")
		def currStudy3 = StudyDataSource.findByShortName("LOI")
		def currStudy4 = StudyDataSource.findByShortName("FCR_DEMO")
		//StudyContext.setStudy("EDIN")
		//session.study = currStudy
		session.myStudies = []
		session.myStudies << currStudy
		session.myStudies << currStudy2
		session.myStudies << currStudy3
		session.myStudies << currStudy4
		//session.dataTypes = AttributeType.findAll().sort { it.longName }
		params = ['outcome':'Relapse','diseases':['BREAST','COLON']]
		QuickStartController controller = new QuickStartController()
		controller.clinicalService = clinicalService
		def results = controller.quickSearch(params);
		println results
	}
		
}
