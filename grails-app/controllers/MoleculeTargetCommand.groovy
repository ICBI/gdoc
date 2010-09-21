
class MoleculeTargetCommand {

	String entity
	String molWeightLow
	String molWeightHigh
	String affinity
	
	static constraints = {
		entity(validator: {val, obj ->
			def invalidChars = ['*','?','~','[',']']
			if(val && (invalidChars.contains(val.trim()) 
					|| val.trim().contains('[')
					|| val.trim().contains(']'))
				){
				return "custom.val"
			}
		})
		molWeightLow(validator: {val, obj ->
			if(val && !val.isDouble()) {
				return "custom.number"
			} else if(val && val.isDouble()){
				if(obj.properties['molWeightHigh'] && (obj.properties['molWeightHigh'].isDouble())){
					if(obj.properties['molWeightHigh'].toDouble() < val.toDouble()){
						return "custom.val"
					}
					else{
						return true
					}
				}else{
					return true
				}
			}else {
				true
			}
		})
		molWeightHigh(validator: {val, obj ->
			if(val && !val.isDouble()) {
				return "custom.number"
			} else if(val && val.isDouble()) {
				if(obj.properties['molWeightLow'] && obj.properties['molWeightLow'].toDouble() > val.toDouble()){
					return "custom.val"
				}else{
					return true
				}
			} else {
				return true
			}
		})
	}
	
}