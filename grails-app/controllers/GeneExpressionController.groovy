import grails.converters.*
import org.apache.commons.math.stat.descriptive.rank.Median

@Mixin(ControllerMixin)
class GeneExpressionController {

	def analysisService
	def savedAnalysisService
	def idService
	def userListService
	def fileBasedAnnotationService
	def htDataService
	
    def index = { 
		if(session.study) {
			session.files = htDataService.getGEDataMap()
			session.dataSetType = session.files.keySet()
			log.debug "my ht files for $session.study = $session.files"
		}
		loadPatientLists()
		[diseases:getDiseases(),myStudies:session.myStudies, params:params]
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
			cmd.groups = cleanedGroups
			def author = GDOCUser.findByUsername(session.userId)
			def tempListFound = cleanedGroups.find{ group ->
				if(userListService.listIsTemporary(group,author)){
					return true
				}
			}
			if(tempListFound){
				tags << Constants.TEMPORARY
			}
		
			def taskId = analysisService.sendRequest(session.id, cmd, tags)
			redirect(controller:'notification')
		}
	}
	
	
	def view = {
		def expressionValues = []
		session.results = savedAnalysisService.getSavedAnalysis(params.id)

		log.debug session.results
		def sampleReporter = [:]
		session.results.analysis.item.dataVectors.each { data ->
			log.debug data.name
			sampleReporter[data.name] = [:]
			data.dataPoints.each { point ->
				sampleReporter[data.name][point.id] = point.x
			}
		}
		log.debug session.results.query.groups
		def median = new Median()
		session.results.query.groups.each { group ->
			def samples = idService.samplesForListName(group)
			log.debug samples
			def valueHash = [:]
			valueHash["group"] = group
			def tempVals = []
			sampleReporter.each { key, value ->
				valueHash[key] = []
				samples.each { sample ->
					if(sampleReporter[key][sample])
						valueHash[key] << sampleReporter[key][sample]
				}
				if(valueHash[key]) {
					valueHash[key] = median.evaluate((Double[])valueHash[key].toArray())
				}
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
