class AnnotationServiceTests extends GroovyTestCase {

	def annotationService
	
    void testGeneAlias() {
		def reporters = annotationService.findReportersForGene('P53')
		reporters.each {
			assertTrue(['201746_at', '211300_s_at'].contains(it))
			
		}
    }

	void testReportersForGeneAndPlatform() {
		StudyContext.setStudy('WANG')
		def reporters = annotationService.findReportersForGeneAndFile('EGFR', 'Wang_binary.Rda')
		println reporters
	}
}
