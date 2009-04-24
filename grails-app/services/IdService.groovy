class IdService {
	
	def patientService
	
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
		def results = patientService.patientsForGdocIds(gdocIds)
		def sampleIds = results.collect { patient ->
			return patient.biospecimens.collect { specimen ->
				if(specimen.type == "LABELED_EXTRACT")
					return specimen.name
			}
		}
		return sampleIds.flatten().grep { it }
	}
}