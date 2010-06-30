import org.springframework.web.context.request.RequestContextHolder as RCH

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
				if(!val.isLong())
					return "custom.long"
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