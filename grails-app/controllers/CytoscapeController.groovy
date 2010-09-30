import java.net.URLEncoder
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class CytoscapeController {
	
	
	def index = {
			log.debug params
			def baseUrl = CH.config.grails.serverURL
			def token = session.userId + "||" + System.currentTimeMillis()
			def sifUrl = baseUrl+"/gdoc/cytoscape/display?token=" + URLEncoder.encode(EncryptionUtil.encrypt(token), "UTF-8")+"&inputFile="+params.sifFile
			def edgeUrl = baseUrl+"/gdoc/cytoscape/display?token=" + URLEncoder.encode(EncryptionUtil.encrypt(token), "UTF-8")+"&inputFile="+params.edgeAttributeFile
			def nodeUrl = baseUrl+"/gdoc/cytoscape/display?token=" + URLEncoder.encode(EncryptionUtil.encrypt(token), "UTF-8")+"&inputFile="+params.nodeAttributeFile
			def geneUrl = baseUrl+"/gdoc/cytoscape/display?token=" + URLEncoder.encode(EncryptionUtil.encrypt(token), "UTF-8")+"&inputFile="+params.geneAttributeFile
			[sifUrl:sifUrl,edgeUrl:edgeUrl,nodeUrl:nodeUrl,geneUrl:geneUrl]
		}
		
		def display = {
			try{
				//def file = new File("/Users/kmr75/Desktop/cytoTests/"+ params.inputFile)
				def file = new File(System.getProperty("java.io.tmpdir") +"/"+params.inputFile)
				byte[] fileBytes = file.readBytes()
					response.outputStream << fileBytes
			}catch(java.io.FileNotFoundException fnf){
				log.debug fnf.toString()
				render "File ($params.inputFile) was not found...is the file name correct?"
			}
		}
		
		def javaPolicy = {
			
		}
}