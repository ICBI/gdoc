class AnalysisCommand {
	
	String baselineGroup
	String groups
	String pvalue
	String foldChange
	String dataFile
	String study
	AnalysisType requestType = (AnalysisType.CLASS_COMPARISON)
	
	static constraints = {
		baselineGroup(blank:false, validator: {val, obj ->
			def duplicateFound = false
			if(obj.properties['groups']){
					def groupz = []
					groupz = obj.properties['groups'] as List
					groupz.each{
					it = it.replace('[','');
					it = it.replace(']','');
					if(it.trim() == obj.properties['baselineGroup']){
						println "found dup: $it is also baseline group...  "+  obj.properties['baselineGroup']
						duplicateFound = true
					}
				}
			}
			if(duplicateFound){
				return "custom.duplicate"
			}
			return true
		})
		
		groups(validator: { val, obj ->
			println "VALUES:  ${val}"
			if(!val) {
				return "custom.size"
			}
			if(val.size() != 1) {
				return "custom.size"
			}
			val.each {list ->
				println val
			}
			return true
		})
		pvalue(blank:false, validator: {val, obj ->
			if(!val.isDouble()) {
				return "custom.number"
			} else {
				return true
			}
		})
		foldChange(blank:false, validator: {val, obj ->
			if(!val.isDouble()) {
				return "custom.number"
			} else {
				return true
			}
		})
	}
	
}