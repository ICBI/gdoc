import grails.converters.*

class GeneExpressionController {

	def analysisService
	def savedAnalysisService
	def idService
	def userListService
	def fileBasedAnnotationService
	
    def index = { 
		if(session.study){
			StudyContext.setStudy(session.study.schemaName)
			def lists = userListService.getAllLists(session.userId,session.sharedListIds)
			def patientLists = []
			patientLists = lists.findAll { item ->
				(item.tags.contains("patient") && item.schemaNames().contains(StudyContext.getStudy()))
			}
			session.patientLists = []
			session.patientLists = patientLists.sort { it.name }
		}
		def diseases = session.myStudies.collect{it.cancerSite}
		diseases.remove("N/A")
		def myStudies = session.myStudies
		[diseases:diseases as Set,myStudies:myStudies, params:params]
	}

	def search = { GeneExpressionCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			def study = StudyDataSource.findBySchemaName(cmd.study)
			redirect(action:'index',id:study.id)
		} else {
			def tags = []
			def cleanedGroups = []
			cmd.groups.each{ g ->
				if(g){
					g.tokenize(",").each{
						it = it.replace('[','');
						it = it.replace(']','');
						cleanedGroups << it
					}
				}		
			}
			
			def author = GDOCUser.findByLoginName(session.userId)
			def tempListFound = cleanedGroups.find{ group ->
				if(userListService.listIsTemporary(group,author)){
					return true
				}
			}
			if(tempListFound){
				tags << Constants.TEMPORARY
			}
		
			def files = MicroarrayFile.findByNameLike('%.Rda')
			cmd.dataFile = files.name
			def taskId = analysisService.sendRequest(session.id, cmd, tags)
			redirect(controller:'notification')
		}
	}
	
	
	def view = {
		def expressionValues = []
		session.results = savedAnalysisService.getSavedAnalysis(params.id)

		log.debug session.results
		def sampleReporter = [:]
		log.debug "VECTORS: " + session.results.analysis.item
		session.results.analysis.item.dataVectors.each { data ->
			log.debug data.name
			sampleReporter[data.name] = [:]
			data.dataPoints.each { point ->
				log.debug "POINT: " +  point.id + " : " + point.x
				sampleReporter[data.name][point.id] = Math.pow(2, point.x)
				log.debug "REPORTER: " +  sampleReporter[data.name]
			}
		}
		log.debug session.results.query.groups
		session.results.query.groups.each { group ->
			def samples = idService.samplesForListName(group)
			log.debug samples
			def valueHash = [:]
			valueHash["group"] = group
			def tempVals = []
			sampleReporter.each { key, value ->
				valueHash[key] = []
				samples.each { sample ->
					valueHash[key] << sampleReporter[key][sample]
				}
				log.debug valueHash[key]
				if(valueHash[key])
					valueHash[key] = valueHash[key].sum() / valueHash[key].size()
				else 
					valueHash[key] = 0
			}
			expressionValues.add(valueHash)
		}
		session.expressionValues = expressionValues
	}
	
	
	def results = {	
		render session.expressionValues as JSON
	}
}
