import org.codehaus.groovy.grails.web.converters.marshaller.json.DomainClassMarshaller
import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import grails.converters.*
import gov.nih.nci.caintegrator.analysis.messaging.ExpressionLookupResult

public class ExpressionLookupResultMarshaller extends DomainClassMarshaller {
	
	ExpressionLookupResultMarshaller(){
	    super(false) //a constructor can call its superclass's constructor if it's
	  }
	
	public boolean supports(Object object) {
		return (object instanceof ExpressionLookupResult)
	}
	
	public void marshalObject(Object value, JSON json) {
		/*
		json.build{
			taskId(value.taskId)
			dataVectors {
				value.dataVectors.each {
					name(it.name)
					dataPoints(it.dataPoints)
				}
			}
		}
		*/
		println "start marshalling data"
		def item = value
		def result = [:]
		def items = []
		result["taskId"] = item.taskId
		item.dataVectors.each { 
			def vector = [:]
			vector["name"] = it.name
			vector["dataPoints"] = it.dataPoints
			items << vector
		}
		result["dataVectors"] = items
		println "end marshalling data " + result
		json.value(result)
	}
}