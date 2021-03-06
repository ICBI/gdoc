import org.springframework.mock.web.MockServletContext


class QuickStartControllerTests extends grails.test.ControllerUnitTestCase {
	def clinicalService
	def biospecimenService
	def quickStartService
	def htDataService
	def session
	def params
	
	void setUp(){
		def da = quickStartService.getDataAvailability()
		def servletContext = new MockServletContext()
		servletContext.setAttribute("dataAvailability", da)
		session = [ : ]
		QuickStartController.metaClass.getSession = { -> session }
		params = [ : ]
		QuickStartController.metaClass.getParams = { -> params }
		
	}
	

    void testQueryForCounts() {
		//def currStudy = StudyDataSource.findByShortName("DESMEDT7390")
		def currStudy2 = StudyDataSource.findByShortName("CRC_PILOT")
		/*def currStudy3 = StudyDataSource.findByShortName("LOI")
		def currStudy5 = StudyDataSource.findByShortName("FCR")
		def currStudy6 = StudyDataSource.findByShortName("WANG")
		def currStudy7 = StudyDataSource.findByShortName("ZHOU")*/
		//session.study = currStudy
		session.myStudies = []
		//session.myStudies << currStudy
	    session.myStudies << currStudy2
		/*session.myStudies << currStudy3
	   	session.myStudies << currStudy5
		session.myStudies << currStudy6
		session.myStudies << currStudy7*/
		
		params = ['molProfilingType':'ALL','action':'quickSearch','controller':'quickStart']
		QuickStartController controller = new QuickStartController()
		controller.quickStartService = quickStartService
		controller.htDataService = htDataService
		def results = controller.quickSearch(params);
    }

	

	void testQueryForOutcomes() { /**
		def currStudy = StudyDataSource.findByShortName("CLARKE-LIU")
		def currStudy2 = StudyDataSource.findByShortName("CRC_PILOT")
		def currStudy3 = StudyDataSource.findByShortName("LOI")
		def currStudy4 = StudyDataSource.findByShortName("SOTIRIOU")
		def currStudy5 = StudyDataSource.findByShortName("DESMEDT")
		def currStudy6 = StudyDataSource.findByShortName("WANG")
		//def currStudy7 = StudyDataSource.findByShortName("ZHOU")
		def currStudy8 = StudyDataSource.findByShortName("ZHANG")
		//StudyContext.setStudy("EDIN")
		session.study = currStudy
		session.myStudies = []
		session.myStudies << currStudy
	    session.myStudies << currStudy2
		session.myStudies << currStudy3
	   	session.myStudies << currStudy4
		session.myStudies << currStudy5
		session.myStudies << currStudy6
		//session.myStudies << currStudy7
		session.myStudies << currStudy8
		//session.dataTypes = AttributeType.findAll().sort { it.longName }
		params = ['outcome':'Relapse','diseases':['BREAST']]
		QuickStartController controller = new QuickStartController()
		controller.clinicalService = clinicalService
		controller.biospecimenService = biospecimenService
		controller.quickStartService = quickStartService
		def results = controller.quickSearch(params);
		println results**/
	}
		
}
