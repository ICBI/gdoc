import grails.converters.*

class GeneExpressionController {

	def analysisService
	def notificationService
	def idService
	def fileBasedAnnotationService
	
    def index = { 
		session.study = StudyDataSource.findByShortName("EDINBURGH")
		StudyContext.setStudy("EDIN")
		def lists = UserList.findAll()
		def patientLists = lists.findAll { item ->
			(item.tags.contains("patient") && item.tags.contains(StudyContext.getStudy()))
		}
		println lists[0].tags
		session.lists = patientLists
	}

	def search = { GeneExpressionCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index')
		} else {
			def taskId = analysisService.sendRequest(session.id, cmd)
			session.taskId = taskId
			session.command = cmd
			println taskId
		}
	}
	
	def view = {
		def expressionValues = []
		session.results = notificationService.getNotifications(session.userId)
		println session.results
		def sampleReporter = [:]
		session.results.dataVectors.each { data ->
			println data.name
			sampleReporter[data.name] = [:]
			data.dataPoints.each { point ->
				println point.id
				sampleReporter[data.name][point.id] = Math.pow(2, point.x)
				println sampleReporter[data.name]
			}
		}
		println session.command.groups
		session.command.groups.each { group ->
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
		
		println expressionValues
		render expressionValues as JSON
	}
}
