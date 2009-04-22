
class IdServiceTests extends GroovyTestCase {

	def idService
	
    void testSampleNamesForGdocIds() {
		StudyContext.setStudy("EDIN")
		def samples = idService.sampleNamesForGdocIds([100000L, 100002L, 100004L])
		println samples
		assertTrue(samples.size() == 9)
    }
}
