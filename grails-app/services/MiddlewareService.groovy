import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpState
import org.apache.commons.httpclient.HttpStatus
import org.apache.commons.httpclient.params.HttpClientParams
import org.apache.commons.httpclient.UsernamePasswordCredentials
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import grails.converters.JSON
import java.net.URLEncoder

class MiddlewareService {
	
	
	def sparqlQuery(selectClause) {
		def url = "${CH.config.middlewareUrl}/sparql/?query="
		println url
		def data
		try {
			def client = new HttpClient()
			HttpClientParams params = client.getParams()
			params.setAuthenticationPreemptive( true )
			HttpState state = client.getState()
			def user = GDOCUser.findByLoginName("gdocUser")
			def credentials =
		     	new UsernamePasswordCredentials(user.loginName, user.password)
			state.setCredentials( null, null, credentials )
			def queryString = 
			    		"PREFIX nci: <http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#>" +
			    		"PREFIX gdoc: <https://demo.gdoc.georgetown.edu/gdoc/GDOCOntology.owl#>" +
			"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>" +
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
			"PREFIX owl:<http://www.w3.org/2002/07/owl#>" +
			selectClause
			def get = new GetMethod(url + URLEncoder.encode(queryString, "UTF-8"))
			def status = client.executeMethod(get)
			println status
		
			if (status != HttpStatus.SC_OK) {
				return "Method failed: " + get.getStatusLine()
			}

			if(get.getResponseBodyAsString().toString()) 
				data = JSON.parse(get.getResponseBodyAsString().toString())
			get.releaseConnection()
		} catch(Exception e) {
			data = "Cannot connect to server, please try again."
		}
		return data
	}
	
	def loadResource(resource, urlParams, userName) {
		
		def data
		def url = "${CH.config.middlewareUrl}/${resource}.json"
		if(urlParams) {
			def tempParams = urlParams.collect { key, value ->
				return "$key=$value"
			}.join("&")
			url = url + "?" + tempParams
		} 
		

		try {
			def client = new HttpClient()
			HttpClientParams params = client.getParams()
			params.setAuthenticationPreemptive( true )
			HttpState state = client.getState()
			def user
			if(userName) {
				user = GDOCUser.findByLoginName(userName)
			} else {
				user = GDOCUser.findByLoginName("gdocUser")
			}
			def credentials =
		     	new UsernamePasswordCredentials(user.loginName, user.password)
			state.setCredentials( null, null, credentials )
			def get = new GetMethod(url)
			println "URL is: $url"
			def status = client.executeMethod(get)
			println "Status is: $status"
		
			if (status != HttpStatus.SC_OK) {
				return "Method failed: " + get.getStatusLine()
			}

			if(get.getResponseBodyAsString().toString()) 
				data = JSON.parse(get.getResponseBodyAsString().toString())
			get.releaseConnection()
		} catch(Exception e) {
			println e
			data = "Cannot connect to server, please try again."
		}
		return data
	}
	
	def postResource(resource, urlParams, userName) {
		
		def data
		def url = "${CH.config.middlewareUrl}/${resource}.json"
		def jsonData
		if(urlParams) {
			jsonData = urlParams as JSON
		} 
		
		def user = GDOCUser.findByLoginName(userName)
		def credentials =
		     new UsernamePasswordCredentials(user.loginName, user.password)
		try {
			def client = new HttpClient()
			HttpClientParams params = client.getParams()
			params.setAuthenticationPreemptive( true )
			HttpState state = client.getState()
			state.setCredentials( null, null, credentials )
			def post = new PostMethod(url)
			post.setRequestHeader("Content-Type", "application/json")
			post.setRequestEntity(new StringRequestEntity(jsonData.toString(), "application/json", null))
			def status = client.executeMethod(post)
			println status
		
			if (status != HttpStatus.SC_OK) {
				return "Method failed: " + post.getStatusLine()
			}

			if(post.getResponseBodyAsString().toString()) 
				data = JSON.parse(post.getResponseBodyAsString().toString())
			post.releaseConnection()
		} catch(Exception e) {
			e.printStackTrace()
			data = "Cannot connect to server, please try again."
		}
		return data
	}
}