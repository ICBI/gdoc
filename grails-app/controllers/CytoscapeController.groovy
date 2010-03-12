import java.net.URLEncoder

class CytoscapeController {
	
	
	def index = {
			println params
			def baseUrl = request.getRequestURL().toString().split(request.getContextPath())[0]
			def token = session.userId + "||" + System.currentTimeMillis()
			def sifUrl = baseUrl+"/gdoc/cytoscape/display?token=" + URLEncoder.encode(EncryptionUtil.encrypt(token), "UTF-8")+"&inputFile="+params.sifFile
			def edgeUrl = baseUrl+"/gdoc/cytoscape/display?token=" + URLEncoder.encode(EncryptionUtil.encrypt(token), "UTF-8")+"&inputFile="+params.edgeAttributeFile
			def nodeUrl = baseUrl+"/gdoc/cytoscape/display?token=" + URLEncoder.encode(EncryptionUtil.encrypt(token), "UTF-8")+"&inputFile="+params.nodeAttributeFile
			println "look for sifFile $sifUrl"
			println "look for edgeFile $edgeUrl"
			println "look for node file $nodeUrl"
			[sifUrl:sifUrl,edgeUrl:edgeUrl,nodeUrl:nodeUrl]
		}
		
		def display = {
			try{
				//def file = new File("/Users/kmr75/Desktop/cytoTests/"+ params.inputFile)
				def file = new File(System.getProperty("java.io.tmpdir") +"/"+params.inputFile)
				byte[] fileBytes = file.readBytes()
					response.outputStream << fileBytes
			}catch(java.io.FileNotFoundException fnf){
				println fnf.toString()
				render "File ($params.inputFile) was not found...is the file name correct?"
			}
		}
}