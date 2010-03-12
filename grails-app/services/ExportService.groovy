class ExportService {
	def interactionService
	
	def exportList(out, listId) {
		def list = UserList.get(listId)
		def items = []
		def strategy = { return [it] }
		def output = ""
		list.listItems.each {
			if(it.metaClass.respondsTo(it, 'join')) {
				output += it.join("\t") + "\n"
			} else { 
				output += it + "\n"
			}
		}
		out << output
	}
	
	def buildCytoscapeFiles(listId){
		def list = UserList.get(listId)
		def cytoscapeFiles = [:]
		//name all files needed
		def sifName = "${listId}-network.sif"
		def edgeAttributeFileName = "${listId}-edgeAttr.EA"
		def nodeAttributeFileName = "${listId}-nodeAttr.NA"
		//create files
		println  "all files going to " + System.getProperty("java.io.tmpdir")
		def sifFile = new File(System.getProperty("java.io.tmpdir")+"/"+sifName)
		def edgeAttributeFile = new File(System.getProperty("java.io.tmpdir")+"/"+edgeAttributeFileName)
		def nodeAttributeFile = new File(System.getProperty("java.io.tmpdir")+"/"+nodeAttributeFileName)
		def items = []
		def strategy = { return [it] }
		if(list.tags.contains("gene")) {
			strategy = { item ->
				return interactionService.getInteractions(item.toUpperCase())
			}
		}
		list.listItems.each {
			def infoMap = strategy(it.value)
			infoMap.each{map ->
				items.add(map)
			}
			
		}
		def output = ""
		edgeAttributeFile.write("PubMedLink (class=java.lang.String)"+"\n")
		nodeAttributeFile.write("Description (class=java.lang.String)"+"\n")
		println "wote edge header"
		items.each { item ->
			println "process $item.key"
			if(item.value.metaClass.respondsTo(item.value, 'join')) {
				item.value.each{ terms ->
					if(terms.metaClass.respondsTo(terms, 'join')) {
						output += terms.join(" ") + "\n"
					}
					 else { 
						output += terms + "\n"
					}
				}
				
			} else { 
				output += item.value + "\n"
			}
			if(item.key == 'sifItems'){
				println "sif $output"
				sifFile.append(output)
			}
			else if (item.key == 'edgeItems'){
				println "edge $output"
				edgeAttributeFile.append(output)
			}
			else if (item.key == 'nodeItems'){
				println "node $output"
				nodeAttributeFile.append(output)
			}
			output = ""
		}
		
		cytoscapeFiles["sifFile"] = sifName
		cytoscapeFiles["edgeAttributeFile"] = edgeAttributeFileName
		println "edge file written"
		cytoscapeFiles["nodeAttributeFile"] = nodeAttributeFileName
		return cytoscapeFiles
	}
	
}