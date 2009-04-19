import grails.converters.*

class KmController {

	def kmService 
	
    def index = {
		def lists = UserList.findAll()
		def patientLists = lists.findAll { item ->
			item.tags.contains("patient")
		}
		session.lists = patientLists
	}
	
	def search = { KmCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index')
		} else {
			def selectedLists = session.lists.findAll { list ->
				cmd.groups.contains(list.name)
			}
			session.selectedLists = selectedLists
		}
	}

	def view = {
		
		def groups = [:]
		def samples = []
		def temp = false
		StudyContext.setStudy("EDINFAKE")
		session.selectedLists.each { list ->
			def tempList = UserList.findAllByName(list.name)
			def ids = tempList.list_items.collectAll { listItem ->
				listItem.value
				
			}.flatten()
			println "ids: " + ids
			ids = ids.sort()
			def patients = Patient.getAll(ids)
			patients.each { patient ->
				if( patient.clinicalData["SURGERY_TO_RR/FU"]) {
					def sample = [:]
					sample["survival"] = patient.clinicalData["SURGERY_TO_RR/FU"].toDouble()
					sample["censor"] = temp
					temp = !temp
					samples << sample
				}
			}
			println samples

			def points = kmService.plotCoordinates(samples)
			groups[list.name] = points
		}
	
		/*
		samples = []
		(1..100).each {
			def sample = [:]
			sample["survival"] = 5000 * Math.random()
			sample["censor"] = temp
			temp = !temp
			samples << sample
		}
		points = kmService.plotCoordinates(samples)
		groups["Group 2"] = points
		*/
		render groups as JSON
	}
}
