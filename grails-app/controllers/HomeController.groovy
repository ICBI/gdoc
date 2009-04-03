class HomeController {
	def feedService
	def feedMap
	
    def index = { 
	 	feedMap = feedService.getFeed()
		if(feedMap!=null){
			print feedMap.size()
		}
	}
	
}
