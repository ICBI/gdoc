import grails.converters.*
import org.json.simple.*

@Mixin(ControllerMixin)
class KmController {

	def kmService 
	def patientService
	def savedAnalysisService
	def annotationService
	def analysisService
	def sessionFactory
	def userListService
	def htDataService
	
    def index = {
		//clinical km setup
		def endpoints = []
		if(session.study) {
			if(params.baselineGroup && params.groups){
				KmCommand cmd  = new KmCommand();
 				def kmgroups = []
			 	kmgroups << params.baselineGroup
				kmgroups << params.groups
				cmd.groups = kmgroups
				flash.cmd = cmd
				flash.message = " Your 2 lists, $cmd.groups have been prepopulated below for km plot"
			}
			StudyContext.setStudy(session.study.schemaName)

			//temporarily restricted to gene expression
			//session.files = htDataService.getHTDataMap()
			session.files = htDataService.getGEDataMap()
			session.dataSetType = session.files.keySet()
			log.debug "my ht files for $session.study = $session.files"
			session.endpoints = KmAttribute.findAll()
			
		}
		loadPatientLists()
		//gene exp setup
		def reporters = []
		if(params.reporters){
			reporters = params.reporters
			log.debug "got these reporters: "+reporters
		}
		return [diseases:getDiseases(),reporters:reporters]
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
		log.debug params
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
		  log.debug "found mean expression for redrawn plot"
		def meanExpression = expValues[0].expression
			def foldChangeGroups = []
			def foldChange = Double.parseDouble(fc)
			foldChangeGroups = kmService.calculateFoldChangeGroupings(reporter,meanExpression,foldChange,geAnalysisId)
			def kmCommand = new KmGeneExpCommand()
			def groups = []
			groups.add(["gt":foldChangeGroups['&gt;' + foldChange]])
			groups.add(["lt":foldChangeGroups['&lt;' +"-" + foldChange]])
			groups.add(["int":foldChangeGroups['between']])
			"ADDING $groups to command"
			kmCommand.geAnalysisId = Integer.parseInt(geAnalysisId)
			def savedAnalysis = SavedAnalysis.get(kmCommand.geAnalysisId)
			kmCommand.geneExpGroups = groups
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
					def tags = []
					tags << "KM"
					tags << "GENE_EXPRESSION"
					def author = GDOCUser.findByUsername(session.userId)
					def list1IsTemp = userListService.listIsTemporary(cmd.groups,author)
					if(list1IsTemp){
						tags << Constants.TEMPORARY
					}
					def taskId = analysisService.sendRequest(session.id, cmd, tags)
					def geAnalysis = savedAnalysisService.getSavedAnalysis(session.userId, taskId)
					log.debug "CHECKING status ${geAnalysis.id} ${taskId}"
					log.debug "after 10, status is: " + geAnalysis.status
					while(geAnalysis.status != 'Complete'){
						Thread.sleep(5000)
						geAnalysis = savedAnalysisService.getSavedAnalysis(geAnalysis.id)
						geAnalysis.reloadData()
						log.debug "CHECKING status ${geAnalysis.id} ${taskId}"
						//log.debug "JSON ${geAnalysis.analysisData} DATA ${geAnalysis.analysis}"
						log.debug "status of geAnalysis after checking savd is: " + geAnalysis.status
					}
					log.debug "analysis COMPLETE"
					log.debug "retrieve expression values"
					def expValues = kmService.findReportersMeanExpression(geAnalysis.id,null)
					def highestMean = expValues[0].expression
					def reporter = expValues[0].reporter
					def foldChangeGroups = []
					def foldChange = 2
					foldChangeGroups = kmService.calculateFoldChangeGroupings(reporter,highestMean,foldChange,geAnalysis.id)
					def groups = []
					groups.add(["gt":foldChangeGroups['&gt;' + foldChange]])
					//log.debug foldChangeGroups['greater'].size()
					groups.add(["lt":foldChangeGroups['&lt;' +"-" + foldChange]])
					//log.debug foldChangeGroups['less'].size()
					groups.add(["int":foldChangeGroups['between']])
					//log.debug foldChangeGroups['less'].size()
					
					cmd.geAnalysisId = geAnalysis.id
					cmd.geneExpGroups = groups
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
		if(isAccessible(params.id)){
			log.debug "IN REPOPULATE"
			session.savedKM = params.id
			def analysis = savedAnalysisService.getSavedAnalysis(session.savedKM)
			if(analysis.studies){
				def kmStudies =[]
				kmStudies = analysis.studies
				if(kmStudies){
					def kmstudy = kmStudies.find{
						if(it)
							return it
					}
					if(kmstudy){
						StudyContext.setStudy(kmstudy.schemaName)
						loadCurrentStudy()
					}
					
				}
			}
		}else{
			log.debug "user CANNOT access analysis $params.id"
			redirect(controller:'policies', action:'deniedAccess')
		}
	}
	
	def view = { 
		if(session.savedKM){
			log.debug "retrieving SAVED KM"
			def analysis = savedAnalysisService.getSavedAnalysis(session.savedKM)
			log.debug analysis.analysisData
			session.savedKM = null
			render analysis.analysisData
		}
		else{
		def groups = [:]
		def cmd = session.command
		def sampleGroups = []
		def att
		def groupHash = [:]
		
		def tags = []
		tags << "KM"
		
		session.selectedLists.each { list ->
			def samples = []
			def tempList = UserList.findByName(list.name)
			if(!tempList){
				log.debug "LIST: ${list.name}"
				
				tempList = list
			}
			log.debug "TEMPLIST $tempList"
			if(cmd instanceof KmCommand){
				def author = GDOCUser.findByUsername(session.userId)
				def list1IsTemp = userListService.listIsTemporary(tempList.name,author)
				if(list1IsTemp){
					tags << Constants.TEMPORARY
				}
			}
			
			def ids = tempList.listItems.collectAll { listItem ->
					listItem.value
				}.flatten()
			log.debug "ids: " + ids
			ids = ids.sort()
			def patients = patientService.patientsForGdocIds(ids)
			def attributes = KmAttribute.findAll()
			
			def censorStrategy = { patient, endpoint ->
				
				att = attributes.find {
					log.debug "COMPARE: ${it.attribute} : $endpoint"
					it.attribute == endpoint
				}
				log.debug "VALUES: ${patient.clinicalDataValues[att.censorAttribute]} : ${att.censorValue}"
				return (patient.clinicalDataValues[att.censorAttribute] == att.censorValue)
			}
			patients.each { patient ->
				if( patient.clinicalDataValues[cmd.endpoint]) {
					def sample = [:]
					sample["survival"] = patient.clinicalData[cmd.endpoint].toDouble()
					sample["censor"] = censorStrategy(patient, cmd.endpoint)
					sample["id"] = patient.id
					samples << sample
				}
			}
			sampleGroups << samples
			groupHash[list.name] = samples
			log.debug "SAMPLES: $groupHash"
			def points = kmService.plotCoordinates(samples)
			groups[list.name] = points
			log.debug "assigned points"
		}
		def pvalue = null
		
	
		if(cmd instanceof KmGeneExpCommand){
			def geInfo = [:]
			geInfo["geAnalysisId"] = cmd.geAnalysisId
			geInfo["reporters"] = cmd.reporters
			geInfo["currentReporter"] = cmd.currentReporter
			geInfo["foldChange"] = cmd.foldChange
			geInfo["geGroups"] = []
			log.debug "GROUPS: ${cmd.geneExpGroups}"
			cmd.geneExpGroups.each{ ids ->
				log.debug "GROUP ${ids}"
				def cleanedIds = []
				if(ids){
/*					ids.split(",").each{
						it = it.replace('[','');
						it = it.replace(']','');
						cleanedIds << it
					}*/
					geInfo["geGroups"] << ids
				}
			}
			groups["geneExpressionInfo"] = geInfo
			log.debug "GE INFO: "
			log.debug groups["geneExpressionInfo"]
			pvalue = computeMultiplePvalues(groupHash)
			groups["study"] = cmd.study
		} else {
			if(sampleGroups[0] && sampleGroups[1]) {
				pvalue = kmService.getLogRankPValue(sampleGroups[0], sampleGroups[1])
			}
		}
		log.debug "PVALUE $pvalue"
		if(!pvalue)
			pvalue = "N/A"
		groups["pvalue"] = pvalue
		groups["endpointDesc"] = att.attributeDescription
		def resultData = groups as JSON
		log.debug resultData
		if(session.redrawnKM){
			log.debug "km has been redrawn, just return result data"
			render resultData
		}else{
			
			def savedAna = savedAnalysisService.saveAnalysisResult(session.userId, resultData.toString(),cmd, tags)
			if(savedAna){
				log.debug "saved km and now returning result data"
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
