import grails.converters.*
import org.json.simple.*

class KmController {

	def kmService 
	def patientService
	def endpoints
	def savedAnalysisService
	def annotationService
	def analysisService
	def sessionFactory
	def userListService
	
    def index = {
		//clinical km setup
		if(params.id) {
			def currStudy = StudyDataSource.get(params.id)
			session.study = currStudy
			StudyContext.setStudy(session.study.schemaName)
		}
		def lists = userListService.getAllLists(session.userId,session.sharedListIds)
		def patientLists = lists.findAll { item ->
			(item.tags.contains("patient") && item.tags.contains(StudyContext.getStudy()))
		}
		if(StudyContext.getStudy() == "EDIN") {
			endpoints = ["SURGERY_TO_DEATH/FU", "SURGERY_TO_RR/FU", "SURGERY_TO_DR/FU"]
		} else if(StudyContext.getStudy() == "RCF") {
			endpoints = ["AGE_AT_DEATH/FU"]
		}
		session.patientLists = patientLists
		
		//gene exp setup
		def reporters = []
		if(params.reporters){
			reporters = params.reporters
			println "got these reporters: "+reporters
		}
		if(reporters){
			println "returning reporters"
		 return [reporters:reporters]
		}
	}
	
	def search = { KmCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index')
		} else {
			def selectedLists = session.patientLists.findAll { list ->
				cmd.groups.toList().contains(list.name)
			}
			session.command = cmd
			session.selectedLists = selectedLists
		}
	}
	
	def searchGE = {}
	
	def redrawGEPlot = {
		println params
		if(params.reporter){
		 def reporter = params.reporter
		 def allReporters = []
		 if(params.allReporters){
				params['allReporters'].tokenize(",").each{
					allReporters.add(it);
				}
		  }
		 
		 def fc = params.foldChange
		 def geAnalysisId = params.geAnalysisId
		/**move the following code into common area, as both this method and submitGEPlot use it
		--------------------------------------------------------------------------------**/
		//TODO - carry mean expressions to flex cmponent and back so no re-calc is necessary
		 def expValues = kmService.findReportersMeanExpression(geAnalysisId,reporter)
		  println "found mean expression for redrawn plot"
		def meanExpression = expValues[0].expression
			def foldChangeGroups = []
			def foldChange = Integer.parseInt(fc)
			foldChangeGroups = kmService.calculateFoldChangeGroupings(reporter,meanExpression,foldChange,geAnalysisId)
			def kmCommand = new KmCommand()
			def groups = []
			groups.add(foldChangeGroups['&gt;' + foldChange])
			groups.add(foldChangeGroups['&lt;' +"-" + foldChange])
			groups.add(foldChangeGroups['between'])
			
			kmCommand.geAnalysisId = Integer.parseInt(geAnalysisId)
			kmCommand.groups = groups
			kmCommand.reporters = allReporters
			kmCommand.foldChange = foldChange;
			kmCommand.currentReporter = reporter
			kmCommand.endpoint = "SURGERY_TO_DEATH/FU"
			session.command = kmCommand
			def tempLists = createTempUserListsForKM(foldChangeGroups)
			session.selectedLists = tempLists
		/**------------------------------------------------**/
		}
		redirect(action:'searchGE')
	}
	
	def submitGEPlot= {
			GeneExpressionCommand cmd ->
				if(cmd.hasErrors()) {
					flash['cmd'] = cmd
					redirect(action:'index')
				} else {
					def taskId = analysisService.sendRequest(session.id, cmd)
					def geAnalysis = savedAnalysisService.getSavedAnalysis(session.userId, taskId)
					println "CHECKING status ${geAnalysis.id} ${taskId}"
					println "after 10, status is: " + geAnalysis.status
					while(geAnalysis.status != 'Complete'){
						Thread.sleep(5000)
						geAnalysis = savedAnalysisService.getSavedAnalysis(geAnalysis.id)
						geAnalysis.reloadData()
						println "CHECKING status ${geAnalysis.id} ${taskId}"
						//println "JSON ${geAnalysis.analysisData} DATA ${geAnalysis.analysis}"
						println "status of geAnalysis after checking savd is: " + geAnalysis.status
					}
					println "analysis COMPLETE"
					println "retrieve expression values"
					def expValues = kmService.findReportersMeanExpression(geAnalysis.id,null)
					def highestMean = expValues[0].expression
					def reporter = expValues[0].reporter
					def foldChangeGroups = []
					def foldChange = 2
					foldChangeGroups = kmService.calculateFoldChangeGroupings(reporter,highestMean,foldChange,geAnalysis.id)
					def kmCommand = new KmCommand()
					def groups = []
					groups.add(foldChangeGroups['&gt;' + foldChange])
					//println foldChangeGroups['greater'].size()
					groups.add(foldChangeGroups['&lt;' +"-" + foldChange])
					//println foldChangeGroups['less'].size()
					groups.add(foldChangeGroups['between'])
					//println foldChangeGroups['less'].size()
					
					kmCommand.geAnalysisId = geAnalysis.id
					kmCommand.groups = groups
					kmCommand.reporters = expValues.collect {
						it.reporter
					}
					kmCommand.foldChange = 2;
					kmCommand.currentReporter = reporter
					kmCommand.endpoint = "SURGERY_TO_DEATH/FU"
					session.command = kmCommand
					def tempLists = createTempUserListsForKM(foldChangeGroups)
					session.selectedLists = tempLists
					redirect(uri:'/km/searchGE')
				}
		//	render(template:"/km/geneExpressionFormKM")
			//redirect(action:'index', params:[ reporters: reporterNames ])
	}
	
	def createTempUserListsForKM(foldChangeGroups){
		def templists = []
		foldChangeGroups.each{name, ids ->
			def tempIds = foldChangeGroups[name]
			def userlist = new UserList(name:name)
			userlist.listItems = []
			tempIds.each{
				def item = new UserListItem(value:it)
				userlist.listItems.add(item)
			}
			templists << userlist
		}
		return templists
	}
	
	//This method strictly repoluates a KM plot. It does not retieve live data,
	//simply data stored at the time of persistance. For this reason,
	//there is no need to grab the lists from database and recalc the results,
	//as this has already been done. 
	def repopulateKM = {
		session.savedKM = params.id
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
			def tempList
			if(list instanceof UserList){
				tempList = list
			}else{
			    tempList = UserList.findAllByName(list.name)
			}
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
			println "SAMPLES: $samples"
			sampleGroups << samples
			def points = kmService.plotCoordinates(samples)
			groups[list.name] = points
			println "assigned points"
		}
		def pvalue = null
		if(sampleGroups[0] && sampleGroups[1])
		pvalue = kmService.getLogRankPValue(sampleGroups[0], sampleGroups[1])
		println "PVALUE $pvalue"
		groups["pvalue"] = pvalue
		if(cmd.reporters && cmd.foldChange){
			def geInfo = [:]
			geInfo["geAnalysisId"] = cmd.geAnalysisId
			geInfo["reporters"] = cmd.reporters
			geInfo["currentReporter"] = cmd.currentReporter
			geInfo["foldChange"] = cmd.foldChange
			groups["geneExpressionInfo"] = geInfo
			println "GE INFO: "
			println groups["geneExpressionInfo"]
		}
		render groups as JSON
	}
	}
}
