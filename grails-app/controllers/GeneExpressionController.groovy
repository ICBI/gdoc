import grails.converters.*

class GeneExpressionController {

    def index = { 
		def lists = UserList.findAll()
		def patientLists = lists.findAll { item ->
			item.tags.contains("patient")
		}
		println lists[0].tags
		session.lists = patientLists
	}

	def search = { GeneExpressionCommand cmd ->
		if(cmd.hasErrors()) {
			flash['cmd'] = cmd
			redirect(action:'index')
		} else {
			//analysisService.sendRequest(session.id, cmd)
		}
	}
	
	def view = {
		def expressionValues = []
		
		["ALL", "No Relapse", "Distal Recurrence", "No DR but had LR or RR"].each { group ->
			def valueHash = [:]
			valueHash["group"] = group
			valueHash['1565483_at'] = Math.round(5000 * Math.random())
			valueHash['1565484_x_at'] = Math.round(6000 * Math.random())
			valueHash['201983_s_at'] = Math.round(10000 * Math.random())
			valueHash['201984_s_at'] = Math.round(30000 * Math.random())
			valueHash['210984_x_at'] = Math.round(7000 * Math.random())
			valueHash['211550_at'] = Math.round(2000 * Math.random())
			valueHash['211551_at'] = Math.round(3000 * Math.random())
			expressionValues.add(valueHash)
		}
		
		println expressionValues
		render expressionValues as JSON
	}
}
