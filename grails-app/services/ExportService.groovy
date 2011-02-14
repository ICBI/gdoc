class ExportService {
	def interactionService
	
	def exportList(out, listId) {
		def list = UserList.get(listId)
		def items = list.listItems
		def output = ""
		if(items.metaClass.respondsTo(items,'join')){
			items.each{ item ->
				output += item.value + "\n"
			}
		}else { 
				output += item.value + "\n"
		}
		
		out << output
	}
	
	def buildCytoscapeFiles(listId){
		def list = UserList.get(listId)
		def cytoscapeFiles = [:]
		//name all files needed
		def sifName = "${listId}-network"+System.currentTimeMillis()+".sif"
		def edgeAttributeFileName = "${listId}-edgeAttr"+System.currentTimeMillis()+".EA"
		def nodeAttributeFileName = "${listId}-nodeAttr"+System.currentTimeMillis()+".NA"
		def geneAttributeFileName = "${listId}-geneNodeAttr"+System.currentTimeMillis()+".NA"
		//create files
		log.debug  "all files going to " + System.getProperty("java.io.tmpdir")
		def sifFile = new File(System.getProperty("java.io.tmpdir")+File.separator+sifName)
		def edgeAttributeFile = new File(System.getProperty("java.io.tmpdir")+File.separator+edgeAttributeFileName)
		def nodeAttributeFile = new File(System.getProperty("java.io.tmpdir")+File.separator+nodeAttributeFileName)
		def geneAttributeFile = new File(System.getProperty("java.io.tmpdir")+File.separator+geneAttributeFileName)
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
		nodeAttributeFile.write("Type (class=java.lang.String)"+"\n")
		geneAttributeFile.write("Annotation (class=java.lang.String)"+"\n")
		//log.debug "wote edge header"
		items.each { item ->
			//log.debug "process $item.key"
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
				//log.debug "sif $output"
				sifFile.append(output)
			}
			else if (item.key == 'edgeItems'){
				//log.debug "edge $output"
				edgeAttributeFile.append(output)
			}
			else if (item.key == 'nodeItems'){
				//log.debug "node $output"
				nodeAttributeFile.append(output)
			}else if(item.key == 'geneItems'){
				//log.debug "gene $output"
				geneAttributeFile.append(output)
			}
			output = ""
		}
		
		cytoscapeFiles["sifFile"] = sifName
		cytoscapeFiles["edgeAttributeFile"] = edgeAttributeFileName
		cytoscapeFiles["nodeAttributeFile"] = nodeAttributeFileName
		cytoscapeFiles["geneAttributeFile"] = geneAttributeFileName
		return cytoscapeFiles
	}
	
}