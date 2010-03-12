import org.genepattern.client.GPClient;
import org.genepattern.webservice.JobResult;
import org.genepattern.webservice.Parameter;

class GenePatternServiceTests extends GroovyTestCase {
	def genePatternService
	
    void testsubmitJob() {
		genePatternService.submitJob("acs224", new GenePatternCommand())
    }
}
