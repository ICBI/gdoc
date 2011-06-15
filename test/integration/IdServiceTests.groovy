
class IdServiceTests extends GroovyTestCase {

	def idService
	
	def benchmark = { closure ->  
	  def start = System.currentTimeMillis()  
	  closure.call()  
	  def now = System.currentTimeMillis()  
	  return now - start  
	}
	
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
	
	void testSampleNamesForGdocIdsInduvimed() {
		StudyContext.setStudy("INDIVDEMO")
		def gdocIds = idService.sampleNamesForGdocIds([113764L, 113776L, 113772L, 113760L, 113770L])
		if(gdocIds){
			println "got all gdocIds:"
			gdocIds.each{
				println it
			}
		}
		
	}
	
	void testSampleNamesForList() {
		StudyContext.setStudy("EDIN")
		
		def duration = benchmark {
			def ids = idService.samplesForListName("dr_no_clarke")
			println ids
		}
		println "execution took ${duration} ms"  
	}
}
