class IdService {
	
	def sampleNamesForGdocIds(gdocIds) {
		def c = Patient.createCriteria()
		def results = c.listDistinct {
			'in'("gdocId", gdocIds)
		}
		println results
		def sampleIds = results.collect { patient ->
			println patient
			return patient.biospecimens.collect { specimen ->
				println specimen
				return specimen.name
			}
		}
		return sampleIds.flatten()
	}
}