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

	void testPvalue() {
		def samples = []
		def temp = false
		(1..100).each {
			def sample = [:]
			sample["survival"] = 100 * Math.random()
			sample["censor"] = temp
			temp = !temp
			samples << sample
		}
		def samples2 = []
		(1..100).each {
			def sample = [:]
			sample["survival"] = 100 * Math.random()
			sample["censor"] = temp
			temp = !temp
			samples2 << sample
		}
		def pvalue = kmService.getLogRankPValue(samples, samples2)
		println pvalue
	}
}