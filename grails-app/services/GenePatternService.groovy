import org.genepattern.client.GPClient;
import org.genepattern.webservice.JobResult;
import org.genepattern.webservice.Parameter;
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH


class GenePatternService {
	
	def idService
	def annotationService
	
	/**
	 * Submit a Job to the GenePattern server
	 *
	 */
	def submitJob(userId, cmd) {
		
		def patientFile = "${CH.config.tempDir}/patients_${System.currentTimeMillis()}.txt"
		def reporterFile = "${CH.config.tempDir}/reporters_${System.currentTimeMillis()}.txt"
		
		writeSampleFile(cmd.groups, patientFile)
		writeReporterFile(cmd.geneList, reporterFile)
		
		GPClient gpClient = new GPClient(CH.config.genePatternUrl, userId, "gp2009");
		Parameter[] parameters = new Parameter[8];
		parameters[0] = new Parameter("input.filename1", patientFile);
		parameters[1] = new Parameter("input.filename2", reporterFile);
		parameters[2] = new Parameter("project.name", "gdoc");
		parameters[3] = new Parameter("array.filename", cmd.dataFile);
		parameters[4] = new Parameter("annotation.filename", "hgu133plus2.annotation.Rda");
		parameters[5] = new Parameter("analysis.name", "${cmd.analysisName}");
		parameters[6] = new Parameter("output.cls.file", "${cmd.analysisName}");
		parameters[7] = new Parameter("output.gct.file", "${cmd.analysisName}.gct");
	 	return gpClient.runAnalysisNoWait("ConvertToGctAndClsFile", parameters)
	}
	
	def checkJobs(userId, jobs) {
		GPClient gpClient = new GPClient(CH.config.genePatternUrl, userId, "gp2009");
		jobs.each { job ->
			def jobStatus = gpClient.isComplete(job.jobId)
			job.complete = jobStatus
		}
		return jobs
	}
	
	/*
	 * Write a patient file in the format that convertToGctAndClsFile accepts.
	 * (i.e. listName=sampleId\tsampleId2\t\n)
	 *
	 */
	def writeSampleFile(patientLists, location) {
		new File(location).withWriter { out ->
		    patientLists.each { list ->
				def samples = idService.samplesForListName(list)
				def listValues = samples.join("\t")
				out.writeLine("${list}=${listValues}")
			}
		}
	}
	
	def writeReporterFile(geneList, location) {
		new File(location).withWriter { out ->
		    if(geneList) {
				def reporters = annotationService.findReportersForGeneList(geneList)
				def listValues = reporters.join("\t")
		        out.writeLine("reporter=${listValues}")
		    } else {
				out.writeLine("reporter=NONE")
			}
		}
	}
}