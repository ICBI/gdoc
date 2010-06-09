class ParamsHelper {
	
	static def itemsToList(items){
		def list = []
		items.tokenize(",").each{
			list << it.trim()
		}
		return list
	}
	
}