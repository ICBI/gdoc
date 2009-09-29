class FeedService{
	
	def getFeed(){
	    def xmlFeed = new XmlParser().parse("https://informatics.lombardi.georgetown.edu/spaces/createrssfeed.action?types=blogpost&spaces=public&sort=modified&title=public+Recent+News+Items&maxResults=15&publicFeed=true&rssType=rss2");
		def feedMap = [:]
			 if(xmlFeed.channel && xmlFeed.channel.item){
				xmlFeed.channel.item.each{
					def newsItem = it;
					feedMap.put(newsItem.title.text(),newsItem.link.text())
				}

			 }
	   return feedMap
	}
	
}