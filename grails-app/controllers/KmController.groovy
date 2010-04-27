import grails.converters.*
import org.json.simple.*

class KmController {

	def kmService 
	def patientService
	def savedAnalysisService
	def annotationService
	def analysisService
	def sessionFactory
	def userListService
	
    def index = {
		//clinical km setup
		def endpoints = []
		if(session.study) {
			StudyContext.setStudy(session.study.schemaName)
			def lists = userListService.getAllLists(session.userId,session.sharedListIds)
			def patientLists = lists.findAll { item ->
				(item.tags.contains("patient") && item.schemaNames().contains(StudyContext.getStudy()))
			}
			session.patientLists = patientLists
			session.endpoints = KmAttribute.findAll()
		}
		
		//gene exp setup
		def reporters = []
		if(params.reporters){
			reporters = params.reporters
			println "got these reporters: "+reporters
		}
		def diseases = session.myStudies.collect{it.cancerSite}
		diseases.remove("N/A")
		return [diseases:diseases as Set,reporters:reporters]
	}
	
	def search = { KmCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			def study = StudyDataSource.findBySchemaName(cmd.study)
			redirect(action:'index',id:study.id)
		} else {
			def selectedLists
			if(cmd.groups.toList().contains('ALL')) {
				selectedLists = cmd.groups.toList()
			} else {
				selectedLists = session.patientLists.findAll { list ->
					cmd.groups.toList().contains(list.name)
				}
			}
			session.command = cmd
			session.selectedLists = selectedLists
			session.redrawnKM = null
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
			def foldChange = Double.parseDouble(fc)
			foldChangeGroups = kmService.calculateFoldChangeGroupings(reporter,meanExpression,foldChange,geAnalysisId)
			def kmCommand = new KmGeneExpCommand()
			def groups = []
			groups.add(foldChangeGroups['&gt;' + foldChange])
			groups.add(foldChangeGroups['&lt;' +"-" + foldChange])
			groups.add(foldChangeGroups['between'])
			kmCommand.geAnalysisId = Integer.parseInt(geAnalysisId)
			def savedAnalysis = SavedAnalysis.get(kmCommand.geAnalysisId)
			kmCommand.groups = groups
			kmCommand.reporters = allReporters
			kmCommand.foldChange = foldChange;
			kmCommand.currentReporter = reporter
			kmCommand.endpoint = savedAnalysis.query.endpoint
			kmCommand.study = StudyContext.getStudy()
			session.command = kmCommand
			def tempLists = createTempUserListsForKM(foldChangeGroups)
			session.selectedLists = tempLists
			session.redrawnKM = true
		/**------------------------------------------------**/
		}
		redirect(action:'searchGE')
	}
	
	/**
	1)run gene expression lookup report
	2)retrieve that GE analysis and find the reporter that
	  with the highest mean expression across all samples (by default). Hold onto
	  that highest mean expression value as well as the reporter that holds it.
	3) look up the expression for each sample based on that reporter. Subtract
	   the mean expression from the actual expression value to determine the 
	   sample's fold change. Based on the fold change parameter (either default to "2" or passed as param),
	   group each sample into one of the 3 following classifications:
	   - sample's fold change > fold change param
	   - sample's fold change < negative fold change param
	   - sample's fold change in between fold change param and negative fold change param
	         (e.g. 2 > 0.56 > -2)
	**/
	def submitGEPlot= {
			KmGeneExpCommand cmd ->
				if(cmd.hasErrors()) {
					flash['cmd'] = cmd
					def study = StudyDataSource.findBySchemaName(cmd.study)
					redirect(action:'index',id:study.id)
				} else {
					def files = MicroarrayFile.findByNameLike('%.Rda')
						println "BEFORE"
					cmd.dataFile = files.name
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
					def groups = []
					groups.add(foldChangeGroups['&gt;' + foldChange])
					//println foldChangeGroups['greater'].size()
					groups.add(foldChangeGroups['&lt;' +"-" + foldChange])
					//println foldChangeGroups['less'].size()
					groups.add(foldChangeGroups['between'])
					//println foldChangeGroups['less'].size()
					
					cmd.geAnalysisId = geAnalysis.id
					cmd.groups = groups
					cmd.reporters = expValues.collect {
						it.reporter
					}
					cmd.foldChange = 2;
					cmd.currentReporter = reporter
					session.command = cmd
					def tempLists = createTempUserListsForKM(foldChangeGroups)
					session.selectedLists = tempLists
					session.redrawnKM = null
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
		println "IN REPOPULATE"
		session.savedKM = params.id
	}
	
	//TODO - decide if we always want to auto-save KM plots. Right now, we do not. They must implicitly call 'save'.
	def view = { 
		if(session.savedKM){
			println "retrieving SAVED KM"
			def analysis = savedAnalysisService.getSavedAnalysis(session.savedKM)
			println analysis.analysisData
			session.savedKM = null
			render analysis.analysisData
		}
		else{
		def groups = [:]
		def cmd = session.command
		def sampleGroups = []
		def att
		def groupHash = [:]
		session.selectedLists.each { list ->
			def samples = []
			def tempList
			if(list instanceof UserList){
				println "LIST: ${list.name}"
				
				tempList = list
			}else{
				println "LIST2: ${list}"
				
			    tempList = UserList.findAllByName(list.name)
			}
			println "TEMPLIST $tempList"
			def ids = tempList.listItems.collectAll { listItem ->
					listItem.value
				}.flatten()
			println "ids: " + ids
			ids = ids.sort()
			def patients = patientService.patientsForGdocIds(ids)
			def attributes = KmAttribute.findAll()
			
			def censorStrategy = { patient, endpoint ->
				
				att = attributes.find {
					println "COMPARE: ${it.attribute} : $endpoint"
					it.attribute == endpoint
				}
				return (patient.clinicalData[att.censorAttribute] == att.censorValue)
			}
			patients.each { patient ->
				if( patient.clinicalData[cmd.endpoint]) {
					def sample = [:]
					sample["survival"] = patient.clinicalData[cmd.endpoint].toDouble()
					sample["censor"] = censorStrategy(patient, cmd.endpoint)
					sample["id"] = patient.id
					samples << sample
				}
			}
			sampleGroups << samples
			groupHash[list.name] = samples
			println "SAMPLES: $groupHash"
			def points = kmService.plotCoordinates(samples)
			groups[list.name] = points
			println "assigned points"
		}
		def pvalue = null
	
		if(cmd instanceof KmGeneExpCommand){
			def geInfo = [:]
			geInfo["geAnalysisId"] = cmd.geAnalysisId
			geInfo["reporters"] = cmd.reporters
			geInfo["currentReporter"] = cmd.currentReporter
			geInfo["foldChange"] = cmd.foldChange
			geInfo["geGroups"] = []
			cmd.groups.each{ ids ->
				def cleanedIds = []
				if(ids){
					ids.tokenize(",").each{
						it = it.replace('[','');
						it = it.replace(']','');
						cleanedIds << it
					}
				}
				geInfo["geGroups"] << cleanedIds		
			}
			groups["geneExpressionInfo"] = geInfo
			println "GE INFO: "
			println groups["geneExpressionInfo"]
			pvalue = computeMultiplePvalues(groupHash)
		} else {
			if(sampleGroups[0] && sampleGroups[1]) {
				pvalue = kmService.getLogRankPValue(sampleGroups[0], sampleGroups[1])
			}
		}
		println "PVALUE $pvalue"
		groups["pvalue"] = pvalue
		groups["endpointDesc"] = att.attributeDescription
		def resultData = groups as JSON
		println resultData
		if(session.redrawnKM){
			println "km has been redrawn, just return result data"
			render resultData
		}else{
			if(savedAnalysisService.saveAnalysisResult(session.userId, resultData.toString(),cmd)){
				println "saved km and now returning result data"
				render resultData
			}
		}
	}
	}
	
	private def computeMultiplePvalues(groups) {
		def gt
		def lt
		def between
		groups.each { 
			if(it.key.contains('&gt;')) {
				gt = it.key
			} else if(it.key.contains('&lt;')) {
				lt = it.key
			} else if(it.key.indexOf("between") > -1) {
				between = it.key
			}
		}
		def values = [:]
		if(gt && between) {
			values[gt + " " + between] = kmService.getLogRankPValue(groups[between], groups[gt])
		}
		if(lt && between) {
			values[lt + " " + between] = kmService.getLogRankPValue(groups[between], groups[lt])
			
		}
		if(lt && gt) {
			values["upAndDown"] = kmService.getLogRankPValue(groups[gt], groups[lt])
			
		}
		return values
	}
	
}
