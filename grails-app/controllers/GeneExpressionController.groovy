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
			valueHash['<a href="http://lpgws.nci.nih.gov/cgi-bin/AffyViewer.cgi?st=1&query=1565483_at&org=1" target="_blank">1565483_at</a>'] = Math.round(5000 * Math.random())
			valueHash['<a href="http://lpgws.nci.nih.gov/cgi-bin/AffyViewer.cgi?st=1&query=1565484_x_at&org=1" target="_blank">1565484_x_at</a>'] = Math.round(6000 * Math.random())
			valueHash['<a href="http://lpgws.nci.nih.gov/cgi-bin/AffyViewer.cgi?st=1&query=201983_s_at&org=1" target="_blank">201983_s_at</a>'] = Math.round(10000 * Math.random())
			valueHash['<a href="http://lpgws.nci.nih.gov/cgi-bin/AffyViewer.cgi?st=1&query=201984_s_at&org=1" target="_blank">201984_s_at</a>'] = Math.round(30000 * Math.random())
			valueHash['<a href="http://lpgws.nci.nih.gov/cgi-bin/AffyViewer.cgi?st=1&query=210984_x_at&org=1" target="_blank">210984_x_at</a>'] = Math.round(7000 * Math.random())
			valueHash['<a href="http://lpgws.nci.nih.gov/cgi-bin/AffyViewer.cgi?st=1&query=211550_at&org=1" target="_blank">211550_at</a>'] = Math.round(2000 * Math.random())
			valueHash['<a href="http://lpgws.nci.nih.gov/cgi-bin/AffyViewer.cgi?st=1&query=211551_at&org=1" target="_blank >211551_at</a>'] = Math.round(3000 * Math.random())
			expressionValues.add(valueHash)
		}
		
		println expressionValues
		render expressionValues as JSON
	}
}
