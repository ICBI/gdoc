import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.UsernamePasswordCredentials
import org.apache.commons.httpclient.methods.GetMethod
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import grails.converters.JSON 

class SampleController {
    def index = { 
		def url = "${CH.config.middlewareUrl}/SampleSummary.json?datasource=HTSR"
		def client = new HttpClient()
		def get = new GetMethod(url)
		client.executeMethod(get)

		println get.getResponseBodyAsString().toString()
		session.summary = JSON.parse(get.getResponseBodyAsString().toString())
	}
}
