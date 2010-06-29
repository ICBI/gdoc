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
			println "FEATURE: ${val}"
			return ((obj.searchType == 'feature') && (val != ''))
		})
		location(validator: { val, obj -> 
			println "location: ${val}"
			return ((obj.searchType == 'location') && (val != ''))
		})
		omicsTypes(validator: { val, obj -> 
			if(obj.omicsData) {
				def session = RCH.requestAttributes.session
				if(!session.study)
					return false
			}
		})
	}
}