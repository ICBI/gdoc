import org.genepattern.client.GPClient;
import org.genepattern.webservice.JobResult;
import org.genepattern.webservice.Parameter;
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH


class GenePatternService {
	
	def submitJob(userId, cmd) {
		GPClient gpClient = new GPClient(CH.config.genePatternUrl, userId, "gp2009");
		Parameter[] parameters = new Parameter[8];
		parameters[0] = new Parameter("input.filename1", "/Users/ashinohara/dev/georgetown/gdoc/test/integration/patients.txt");
		parameters[1] = new Parameter("input.filename2", "/Users/ashinohara/dev/georgetown/gdoc/test/integration/reporters.txt");
		parameters[2] = new Parameter("project.name", "gdoc");
		parameters[3] = new Parameter("array.filename", "EdinPlier_22APR2009.Rda");
		parameters[4] = new Parameter("annotation.filename", "hgu133plus2.annotation.Rda");
		parameters[5] = new Parameter("analysis.name", "test");
		parameters[6] = new Parameter("output.cls.file", "test");
		parameters[7] = new Parameter("output.gct.file", "test.gct");
		println parameters
	 	def preprocess = gpClient.runAnalysisNoWait("ConvertToGctAndClsFile", parameters)
	}
}