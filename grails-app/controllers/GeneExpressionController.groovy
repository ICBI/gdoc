import grails.converters.*

class GeneExpressionController {

	def analysisService
	def savedAnalysisService
	def idService
	def userListService
	def fileBasedAnnotationService
	
    def index = { 
		session.study = StudyDataSource.get(params.id)
		StudyContext.setStudy(session.study.schemaName)
		def lists = userListService.getAllLists(session.userId,session.sharedListIds)
		def patientLists = lists.findAll { item ->
			(item.tags.contains("patient") && item.tags.contains(StudyContext.getStudy()))
		}
		session.patientLists = patientLists
	}

	def search = { GeneExpressionCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			def study = StudyDataSource.findBySchemaName(cmd.study)
			redirect(action:'index',id:study.id)
		} else {
			def files = MicroarrayFile.findByNameLike('%.Rda')
			cmd.dataFile = files.name
			def taskId = analysisService.sendRequest(session.id, cmd)
			redirect(controller:'notification')
		}
	}
	
	
	def view = {
		def expressionValues = []
		session.results = savedAnalysisService.getSavedAnalysis(params.id)

		println session.results
		def sampleReporter = [:]
		println "VECTORS: " + session.results.analysis.item
		session.results.analysis.item.dataVectors.each { data ->
			println data.name
			sampleReporter[data.name] = [:]
			data.dataPoints.each { point ->
				println "POINT: " +  point.id + " : " + point.x
				sampleReporter[data.name][point.id] = Math.pow(2, point.x)
				println "REPORTER: " +  sampleReporter[data.name]
			}
		}
		println session.results.query.groups
		session.results.query.groups.each { group ->
			def samples = idService.samplesForListName(group)
			println samples
			def valueHash = [:]
			valueHash["group"] = group
			def tempVals = []
			sampleReporter.each { key, value ->
				valueHash[key] = []
				samples.each { sample ->
					valueHash[key] << sampleReporter[key][sample]
				}
				println valueHash[key]
				valueHash[key] = valueHash[key].sum() / valueHash[key].size()
			}
			expressionValues.add(valueHash)
		}
		session.expressionValues = expressionValues
	}
	
	
	def results = {	
		render session.expressionValues as JSON
	}
}
