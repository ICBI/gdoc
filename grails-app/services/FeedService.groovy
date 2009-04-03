class FeedService{
	
def getFeed(){
    def xmlFeed = new XmlParser().parse("http://www.ncbi.nlm.nih.gov/feed/rss.cgi?ChanKey=PubMedNews");

	      def feedMap = [:]
		
		//(0..< xmlFeed.channel.item.size()).each
	      (0..< 10).each {

	         def item = xmlFeed.channel.item.get(it);
			// RSSFeed feed = new RSSFeed( item.title.text(), item.link.text(),
	         //    item.description.text(), item.pubDate.text() )
	        feedMap.put(item.title.text(),item.link.text())
	      }

	      return feedMap
	
}
}