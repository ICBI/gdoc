class IdService {
	
	def patientService
	
	def binaryFileIds
	
	def samplesForListName(listName) {
		def lists = UserList.findAll()
		def list = lists.find { item ->
			item.name == listName
		}
		
		def listValues = list.listItems.collect {item ->
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
		def ids = []
		sampleIds = sampleIds.flatten().grep { it }
		return sampleIds.intersect(binaryFileIds)
	}
	
	
	def gdocIdsForSampleNames(sampleNames){
		def c = Biospecimen.createCriteria()
	    def	samples = c.listDistinct {
			'in'("name", sampleNames)
		}
		if(samples){
			Set gdocIds = samples.collect{ sample ->
				sample.patient.gdocId
			}
			return gdocIds.flatten()
		}
		return samples
	}
}