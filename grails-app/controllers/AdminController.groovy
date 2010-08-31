class AdminController {
	def quickStartService
	def cleanupService
	
	def index = {
		def dataAvail = servletContext.getAttribute("dataAvailability")
		def loadedStudies = []
		dataAvail['dataAvailability'].each{elm ->
			def studyName = elm.find{ key, value ->
				if(key == 'STUDY'){
					loadedStudies << value
				}
			}
		}
		if(loadedStudies)
		loadedStudies.sort()
		[loadedStudies:loadedStudies]
	}
	
	def reload = {
		log.debug("reload available data")
		def da = quickStartService.getDataAvailability()
		servletContext.setAttribute("dataAvailability", da)
		log.debug("finished reloading data")
		redirect(action:index)
		return 
	}
	
	
}