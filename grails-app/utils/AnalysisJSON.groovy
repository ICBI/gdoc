import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import gov.nih.nci.caintegrator.analysis.messaging.ExpressionLookupResult
import java.util.Map
import java.util.HashMap

class AnalysisJSON extends grails.converters.JSON {

	public AnalysisJSON() { 
		super()
	}

	public AnalysisJSON(Object target) { 
		super(target)
	}

	@Override public void bean(Object o) throws ConverterException { 
		if(o instanceof ExpressionLookupResult) {
			def item = o
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
			super.value(result)
		} else {
			super.bean(o)
		}
	}

}