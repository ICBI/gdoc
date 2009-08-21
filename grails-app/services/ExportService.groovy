class ExportService {
	def interactionService
	
	def exportList(out, listId) {
		def list = UserList.get(listId)
		def items = []
		def strategy = { return [it] }
		if(list.tags.contains("gene")) {
			strategy = { item ->
				return interactionService.getInteractions(item)
			}
		}
		list.listItems.each {
			items.addAll(strategy(it.value))
		}
		def output = ""
		items.each {
			if(it.metaClass.respondsTo(it, 'join')) {
				output += it.join("\t") + "\n"
			} else { 
				output += it + "\n"
			}
		}
		out << output
	}
}