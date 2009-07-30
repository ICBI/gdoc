import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpState
import org.apache.commons.httpclient.HttpStatus
import org.apache.commons.httpclient.params.HttpClientParams
import org.apache.commons.httpclient.UsernamePasswordCredentials
import org.apache.commons.httpclient.methods.GetMethod
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import grails.converters.JSON 

class SampleController {
    def index = { 
		def url = "${CH.config.middlewareUrl}/SampleSummary.json?datasource=HTSR"
		def user = GDOCUser.findByLoginName(session.userId)
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
		 	flash.error = "Method failed: " + get.getStatusLine()
			return
		}
		println get.getResponseBodyAsString().toString()
		if(get.getResponseBodyAsString().toString()) 
			session.summary = JSON.parse(get.getResponseBodyAsString().toString())
		get.releaseConnection()
		
	}
}
