class PatientService {
	static PAGE_SIZE = 1000
    boolean transactional = true

    def patientsForGdocIds(gdocIds) {
		println "GDOC: $gdocIds"
		def allPatients
		gdocIds = gdocIds.collect {
			it.toLong()
		}
		def patients = []
		def index = 0;
		while(index < gdocIds.size()) {
			def c = Patient.createCriteria()
			def patientsLeft = gdocIds.size() - index
			def tempPatients
			if(patientsLeft > PAGE_SIZE) {
				def tempIds = (gdocIds.getAt(index..<(index + PAGE_SIZE)))
				tempPatients = c.listDistinct {
					'in'("gdocId", tempIds)
				}
				patients.addAll(tempPatients)
				index += PAGE_SIZE
			} else {
				def tempIds = (gdocIds.getAt(index..<gdocIds.size()))
				tempPatients = c.listDistinct {
					'in'("gdocId", tempIds)
				}
				patients.addAll(tempPatients)
				index += patientsLeft
			}
		}
		return patients
		
    }
}
