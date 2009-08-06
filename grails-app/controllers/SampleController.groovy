import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpState
import org.apache.commons.httpclient.HttpStatus
import org.apache.commons.httpclient.params.HttpClientParams
import org.apache.commons.httpclient.UsernamePasswordCredentials
import org.apache.commons.httpclient.methods.GetMethod
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import grails.converters.JSON 

class SampleController {
	def middlewareService
    def index = { 

		def returnData = middlewareService.loadResource("Sample", ["datasource": params.id], session.userId)
		if(returnData && returnData instanceof Map) {
			session.summary = returnData
		} else {
			session.summary = null
			flash.error = "Error communicating with server.  Please try again."
		}
	}
}
