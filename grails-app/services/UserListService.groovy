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
		def author = GDOCUser.findByLoginName(userId)
		def lists = UserList.findAllByAuthor(author)
		println lists
		return lists
	}
	
	def getSharedListIds(userId){
		def sharedListIds = []
		sharedListIds = securityService.getSharedItemIds(userId, UserList.class.name)
		return sharedListIds
	}
	
	def uniteLists(name,author,ids){
		def items = new ArrayList<String>();
	        ids.each{
	            UserList list = UserList.get(it);
	            if(!list.listItems.isEmpty()){
	                items.addAll(list.listItems.collect{it.value});
	            }
	        }
	    Set<String> unitedSet = new HashSet<String>(items);
		if(unitedSet.isEmpty()){
			println "no values united"
			return null
		}else{
			def userList = new UserList(name:name,author:author);
			unitedSet.each{
				userList.addToListItems(new UserListItem(value:it.trim()));
			}
			println "Union of lists: " + unitedSet
			return userList
		}
	}
	
	def diffLists(name,author,ids){
		def list1 = UserList.get(ids.toArray()[0]);
		def items1 = []
		list1.listItems.each{
			print it.value + ","
			items1 << it.value
		}
		def items2 = []
		def list2 = UserList.get(ids.toArray()[1]);
		list2.listItems.each{
			print it.value + ","
			items2 << it.value
		}
		
		
		def diff = (items1 as Set) + items2
		println "so far: " + diff
		def tmp = items1 as Set
		tmp.retainAll(items2)
		diff.removeAll(tmp)
		if(diff.isEmpty()){
			println "no difference"
			return null
		}else{
			def userList = new UserList(name:name,author:author);
			diff.each{
				userList.addToListItems(new UserListItem(value:it.trim()));
			}
			println "difference of lists: " + diff
			return userList
		}
	}
	
	def intersectLists(name,author,ids){ 
		def items = new ArrayList<String>();
		List<UserList> lists = new ArrayList<UserList>();
		ids.each{
		      UserList list = UserList.get(it);
			  lists.add(list);
		      if(!list.listItems.isEmpty()){
		          list.listItems.each{ itemV ->
				  	items << itemV.value
				  }
		      }
		}
		def intersectedList = new HashSet<String>(items);
		
		for(UserList ul : lists){
					def values = []
					ul.listItems.each{
						values << it.value
					}
					//println "values " + values
		            intersectedList.retainAll(values);
		 }
		 items.clear();
		 items.addAll(intersectedList);
		if(items.isEmpty()){
			println "no intersection"
			return null
		}else{
			def userList = new UserList(name:name,author:author);
			items.each{
				userList.addToListItems(new UserListItem(value:it.trim()));
			}
			println "Intersection list: " + intersectedList
			return userList
		}
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