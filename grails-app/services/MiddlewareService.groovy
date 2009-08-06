import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpState
import org.apache.commons.httpclient.HttpStatus
import org.apache.commons.httpclient.params.HttpClientParams
import org.apache.commons.httpclient.UsernamePasswordCredentials
import org.apache.commons.httpclient.methods.GetMethod
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import grails.converters.JSON

class MiddlewareService {
	
	def loadResource(resource, urlParams, userName) {
		def url = "${CH.config.middlewareUrl}/${resource}.json"
		if(urlParams) {
			def tempParams = urlParams.collect { key, value ->
				return "$key=$value"
			}.join("&")
			url = url + "?" + tempParams
		} 
		
		def user = GDOCUser.findByLoginName(userName)
		def credentials =
		     new UsernamePasswordCredentials(user.loginName, user.password)

		def client = new HttpClient()
		HttpClientParams params = client.getParams()
		params.setAuthenticationPreemptive( true )
		HttpState state = client.getState()
		state.setCredentials( null, null, credentials )
		def get = new GetMethod(url)
		def status = client.executeMethod(get)
		println status
		
		if (status != HttpStatus.SC_OK) {
			return "Method failed: " + get.getStatusLine()
		}
		def data
		if(get.getResponseBodyAsString().toString()) 
			data = JSON.parse(get.getResponseBodyAsString().toString())
		get.releaseConnection()
		return data
	}
}