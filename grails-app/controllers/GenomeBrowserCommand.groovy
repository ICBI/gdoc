import org.springframework.web.context.request.RequestContextHolder as RCH
import java.text.NumberFormat
import java.text.ParseException
import java.text.ParsePosition

class GenomeBrowserCommand {
	String searchType
	String feature
	String chromosome
	String location
	String hiddenLocation
	String trackMatch
	Boolean omicsData
	String omicsTypes
	
	static constraints = {
		searchType(blank:false)
		feature(validator: { val, obj ->
			if((obj.searchType == 'feature') && (val == ''))
				return "custom.blank"
			return true
		})
		location(validator: { val, obj -> 
			if((obj.searchType == 'location')) {
				if(val == '')
					return "custom.blank"
				NumberFormat nf = NumberFormat.getInstance()
				nf.setParseIntegerOnly(true)
				ParsePosition position = new ParsePosition(0);
				try {
					nf.parse(val, position)
					if (position.getIndex() != val.length()) {
					    return "custom.long"
					}
				} catch (ParseException e) {
					return "custom.long"
				}
			}
			return true
		})
		omicsTypes(validator: { val, obj -> 
			if(obj.omicsData) {
				def session = RCH.requestAttributes.session
				if(!session.study)
					return "custom.study"
			}
		})
	}
}