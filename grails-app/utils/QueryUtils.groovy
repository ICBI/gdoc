class QueryUtils {
	private static PAGE_SIZE = 1000
	
	static def paginateResults(items, queryClosure) {
		def resultItems = []
		def index = 0;
		while(index < items.size()) {
			def itemsLeft = items.size() - index
			def tempItems
			if(itemsLeft > PAGE_SIZE) {
				def tempIds = (items.getAt(index..<(index + PAGE_SIZE)))
				tempItems = queryClosure(tempIds)
				resultItems.addAll(tempItems)
				index += PAGE_SIZE
			} else {
				def tempIds = (items.getAt(index..<items.size()))
				tempItems = queryClosure(tempIds)
				resultItems.addAll(tempItems)
				index += itemsLeft
			}
		}
		return resultItems
	}
}