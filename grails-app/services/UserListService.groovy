class UserListService{

def securityService

	def getAllLists(userId,sharedIds){
		def lists = getUserLists(userId)
		def listIds = []
		def sharedListIds = []
		sharedListIds = sharedIds
		if(!sharedListIds){
			sharedListIds = getSharedListIds(userId)
		}
		if(lists.metaClass.respondsTo(lists, "size")) {
				lists.each{
					listIds << it.id.toString()
				}
		} else {
				listIds << lists.id.toString()
		}
			
		def sharedLists = []
		//until we modify ui, just add shared lists to 'all' lists
		sharedListIds.each{
			if(!(listIds.contains(it))){
			def foundList = UserList.get(it.toString())
			if(foundList){
				lists << foundList
			}
		   }
		}
		lists = lists.sort { one, two ->
			def dateOne = one.dateCreated
			def dateTwo = two.dateCreated
			return dateTwo.compareTo(dateOne)
		}
		return lists
	}
	
	def getUserLists(userId){
		def lists = GDOCUser.findByLoginName(userId).lists()
		return lists
	}
	
	def getSharedListIds(userId){
		def sharedListIds = []
		sharedListIds = securityService.getSharedItemIds(userId, UserList.class.name)
		return sharedListIds
	}
	

def join = { 
	if(params["listAction"].equals('intersect')){
		println "intersect lists:"
		params["userListId"].each{
			println it
		}
	}
	if(params["listAction"].equals('join')){
		println "join lists:"
		params["userListId"].each{
			println it
		}
	}
	if(params["listAction"].equals('diff')){
		println "diff lists:"
		params["userListId"].each{
			println it
		}
	}
	redirect(action:list,params:params)
}

def calculateVenn = {
		def list= ['VEGF','EGFR','BRCA1','BRCA1','ER+','ER-','FCR','HGI']
		def list2= ['VEGF','EGFR','BRCA1','SCF7', 'QSR6','HGI']
		assert list.retainAll( list2 )
		    //remove 'b' and 'z', return true because list changed
		assert list == ['VEGF','EGFR','BRCA1','BRCA1','HGI']
		println list
		def listSet = list as Set
		println listSet
	
}

}