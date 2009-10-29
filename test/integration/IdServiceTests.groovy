
class IdServiceTests extends GroovyTestCase {

	def idService
	
    void testSampleNamesForGdocIds() {
		StudyContext.setStudy("EDIN")
		def samples = idService.sampleNamesForGdocIds([100000L, 100002L, 100004L])
		println samples
		assertTrue(samples.size() == 2)
    }

	void testGdocIdsForSampleNames(){
		StudyContext.setStudy("EDIN")
		def gdocIds = idService.gdocIdsForSampleNames(['Edinburgh_0016_SEL', 'Edinburgh_1013_SEL', 'Edinburgh_1027_SEL', 'Edinburgh_1050_SEL', 'Edinburgh_0044_SEL'])
		if(gdocIds){
			println "got all gdocIds:"
			gdocIds.each{
				println it
			}
		}
	}
}
