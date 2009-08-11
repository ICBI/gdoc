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
	def summaryService
	
	def index = {
		def summary = summaryService.sampleSummary()
		def datasources = []
		def options = [:]
		summary.each { item ->
			println item
			datasources << item.key
			item.value.each { option ->
				println option
				if(!options[option.key]) {
					options[option.key] = new HashSet()
				}
				option.value.each { 
					it.each { value ->
						options[option.key] << value.key
					}
				}
			}
		}
		println "OPTIONS: " + options
		session.options = options
		session.datasources = datasources
	}
	
    def search = { 
		println "PARAMS: $params"
		params.keySet().removeAll( ['action', 'submit', 'controller'] as Set )
		session.sampleQuery = params
		println "PARAMS2: $params"
		def returnData = middlewareService.postResource("Sample", params, session.userId)
		if(returnData && returnData instanceof Map) {
			session.summary = returnData
		} else {
			session.summary = null
			flash.error = "Error communicating with server.  Please try again."
		}
	}
	
}
