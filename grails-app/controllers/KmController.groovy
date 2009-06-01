import grails.converters.*

class KmController {

	def kmService 
	def patientService
	def endpoints
	def savedAnalysisService
	
    def index = {
		if(params.id) {
			def currStudy = StudyDataSource.get(params.id)
			session.study = currStudy
			StudyContext.setStudy(session.study.schemaName)
		}
		def lists = GDOCUser.findByLoginName(session.userId).lists()
		def patientLists = lists.findAll { item ->
			(item.tags.contains("patient") && item.tags.contains(StudyContext.getStudy()))
		}
		if(StudyContext.getStudy() == "EDIN") {
			endpoints = ["SURGERY_TO_DEATH/FU", "SURGERY_TO_RR/FU", "SURGERY_TO_DR/FU"]
		} else if(StudyContext.getStudy() == "RCF") {
			endpoints = ["AGE_AT_DEATH/FU"]
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
			session.command = cmd
			session.selectedLists = selectedLists
		}
	}
	
	//This method strictly repoluates a KM plot. It does not retieve live data,
	//simply data stored at the time of persistance. For this reason,
	//there is no need to grab the lists from database and recalc the results,
	//as this has already been done. 
	def repopulateKM = {
		session.savedKM = params.savedId
	}
	
	//TODO - decide if we always want to auto-save KM plots. Right now, we do not. They must implicitly call 'save'.
	def view = { 
		if(session.savedKM){
			def analysis = savedAnalysisService.getSavedAnalysis(session.savedKM)
			println analysis.analysisData
			session.savedKM = null
			render analysis.analysisData
		}
		else{
		def groups = [:]
		def cmd = session.command
		def sampleGroups = []
		session.selectedLists.each { list ->
			def samples = []
			def tempList = UserList.findAllByName(list.name)
			def ids = tempList.listItems.collectAll { listItem ->
				listItem.value
				
			}.flatten()
			println "ids: " + ids
			ids = ids.sort()
			def patients = patientService.patientsForGdocIds(ids)
			def censorStrategy = { patient, endpoint ->
				if(endpoint == "SURGERY_TO_DEATH/FU")
					return (patient.clinicalData["VITAL_STATUS"] == "DEAD")
				else if (endpoint == "SURGERY_TO_RR/FU")
					return (patient.clinicalData["RR"] == "YES")
				else if (endpoint == "SURGERY_TO_DR/FU")
					return (patient.clinicalData["DR"] == "YES")
				else if (endpoint == "AGE_AT_DEATH/FU")
					return (patient.clinicalData["VITAL_STATUS"] == "DEAD")
			}
			patients.each { patient ->
				if( patient.clinicalData[cmd.endpoint]) {
					def sample = [:]
					sample["survival"] = patient.clinicalData[cmd.endpoint].toDouble()
					sample["censor"] = censorStrategy(patient, cmd.endpoint)
					samples << sample
				}
			}
			println samples
			sampleGroups << samples
			def points = kmService.plotCoordinates(samples)
			groups[list.name] = points
		}
		def pvalue = kmService.getLogRankPValue(sampleGroups[0], sampleGroups[1])
		println "PVALUE $pvalue"
		groups["pvalue"] = pvalue
		render groups as JSON
	}
	}
}
