class IdService {
	
	def samplesForListName(listName) {
		def lists = UserList.findAll()
		def list = lists.find { item ->
			item.name == listName
		}
		
		def listValues = list.list_items.collect {item ->
			item.value.toLong()
		}
		
		return sampleNamesForGdocIds(listValues)
	}
	
	def sampleNamesForGdocIds(gdocIds) {
		def c = Patient.createCriteria()
		println "GDOC: $gdocIds"
		def results = c.listDistinct {
			'in'("gdocId", gdocIds)
		}
		def sampleIds = results.collect { patient ->
			return patient.biospecimens.collect { specimen ->
				if(specimen.type == "LABELED_EXTRACT")
					return specimen.name
			}
		}
		return sampleIds.flatten().grep { it }
	}
}