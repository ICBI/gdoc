import grails.test.*

class KmServiceTests extends GroovyTestCase {
	
	def kmService
	
    protected void setUp() {
        super.setUp()
		kmService = new KmService()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testPlot() {
		def samples = []
		def temp = false
		(1..100).each {
			def sample = [:]
			sample["survival"] = 5000 * Math.random()
			sample["censor"] = temp
			temp = !temp
			samples << sample
		}
		println samples
		def points = kmService.plotCoordinates(samples)
		println points
    }
}