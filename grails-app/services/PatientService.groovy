class PatientService {

    boolean transactional = true

    def patientsForGdocIds(gdocIds) {
		def c = Patient.createCriteria()
		println "GDOC: $gdocIds"
		gdocIds = gdocIds.collect {
			it.toLong()
		}
		return c.listDistinct {
			'in'("gdocId", gdocIds)
		}
		
    }
}
