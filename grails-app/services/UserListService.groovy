import grails.converters.*

class UserListService{

def securityService
def drugDiscoveryService

	def getAllLists(userId,sharedIds){
		def user = GDOCUser.findByLoginName(userId)
		user.refresh()
		def lists = getUserLists(user.loginName)
		def listIds = []
		def sharedListIds = []
		sharedListIds = sharedIds
		if(!sharedListIds){
			sharedListIds = getSharedListIds(userId)
		}else{
			println "use shared lists ids previously retrieved"
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
	
	def filterLists(timePeriod, allLists){
		if(timePeriod == "all"){
			return allLists
		}
		else{
		def tp = Integer.parseInt(timePeriod)
		def filteredLists = []
		def today = new Date()
		allLists.each{ list ->
			if(today.minus(list.dateCreated) <= tp){
				filteredLists << list
			}
		}
		return filteredLists
		}
	}
	
	def getPaginatedLists(ids,offset){
		def idsString = ids.toString().replace("[","")
		idsString = idsString.replace("]","")
		def query = "from UserList as ul where ul.id in ("+idsString+") order by ul.dateCreated desc"
		def al = []
		def pagedLists =
		UserList.createCriteria().list(
			max:10,
			offset:offset)
			{
			'in'('id',ids)
			}
		
		al = UserList.findAll(query,[max:10,offset:offset])
		pagedLists.clear()
		pagedLists.addAll(al)
		println "myLists -> $pagedLists as Paged set"
		return pagedLists
	}
	
	def getUserLists(userId){
		def author = GDOCUser.findByLoginName(userId)
		def lists = UserList.findAllByAuthor(author)
		println "size=" + lists.size()
		return lists
	}
	
	def getSharedListIds(userId){
		def sharedListIds = []
		sharedListIds = securityService.getSharedItemIds(userId, UserList.class.name)
		return sharedListIds
	}
	
	def uniteLists(name,author,ids){
		return performOperation(name, author, ids, { items, list ->
			items.addAll(list.listItems.collect{it.value})
		})
	}
	
	def diffLists(name,author,ids){
		return performOperation(name, author, ids, { items, list ->
			items.removeAll(list.listItems.collect{it.value})
		})
	}
	
	def intersectLists(name,author,ids){ 
		return performOperation(name, author, ids, { items, list ->
			items.retainAll(list.listItems.collect{it.value})
		})
	}
	
	def performOperation(name, author, ids, strategy) {
		Set<String> unitedTags = []
		Set unitedStudies = []
		def items = new ArrayList<String>();
        ids.each{
            UserList list = UserList.get(it);
            if(!list.listItems.isEmpty()){
				if(items.isEmpty())
					items.addAll(list.listItems.collect{it.value})
				else 
					strategy(items, list)
            }
			if(list.tags){
				list.tags.tokenize(" ").each{ tag ->
					unitedTags << tag
				}
			}
			list.studies.each {
				unitedStudies << it.schemaName
			}
        }
	    Set<String> unitedSet = new HashSet<String>(items);
		if(unitedSet.isEmpty()){
			return null
		}else{
			def userList = createList(author, name, unitedSet, unitedStudies, unitedTags)
			return userList
		}
	}
	def vennDiagram(name,author,ids){
		//need to calculate relative size by comparison
		def vennCalculations = []
		List<UserList> lists = new ArrayList<UserList>();
		def vids = []
		if(ids.metaClass.respondsTo(ids, "max")) {
			ids.each{
				  println it + " has been sent"
			      UserList list = UserList.get(it);
				  lists.add(list);
			}
		}
		else{
			UserList list = UserList.get(ids);
			lists.add(list);
		}
		
		
		
		def sortedListsBySize = lists.sort{ l ->
			if(l.listItems){
				-l.listItems.size()
			}
		}
		
		sortedListsBySize.each{
			if(it.listItems){
				println it.name + ": " + it.listItems.size()
			}else{
				println it.name + ": has no list items"
				return null;
			}
		}
		
		
		//if only 2 lists
		if(sortedListsBySize.size() == 2){
			println "venn diagram of 2 lists"
			def firstList = sortedListsBySize.toArray()[0];
			def secondList = sortedListsBySize.toArray()[1];
			def relSize1and2 = 	(secondList.listItems.size()/firstList.listItems.size()) * 100;
			
			//circle 1
			def circle1 = [:]
			def circle1Size = 100;
			println "circle 1:" + firstList.name + "," +circle1Size + "%"
			//circle 2
			def circle2 = [:]
			def circle2Size = relSize1and2;
			println "circle 2:" + secondList.name + "," +circle2Size + "%"
			
			//when sizes are determined,calculate pct comparisons
			def compLists1and2 = []
			compLists1and2 << firstList
			compLists1and2 << secondList
			def pct1and2 = calculatePctBetween2Lists(compLists1and2)
			
			//create valueMap and add to result
			circle1["circle1"] =  createCircleValues(firstList,circle1Size,pct1and2[1],null);
			circle2["circle2"] =  createCircleValues(secondList,circle2Size,null,null);
			//take care of all intersection result
			def allIntersections = [:]
			def allValueMap = [:]
			allValueMap["items"] = pct1and2.toArray()[0]
			allValueMap["circleInt"] = pct1and2.get(1)
			println pct1and2.get(1).class
			allIntersections["allCircles"]  = allValueMap
			
			vennCalculations << circle1
			vennCalculations << circle2
			vennCalculations << allIntersections
			println "return venn Calculations"
			return vennCalculations as JSON
		}
		//if 3 lists
		else if(sortedListsBySize.size() == 3){
			println "venn diagram of 3 lists"
			def firstList = sortedListsBySize.toArray()[0];
			def secondList = sortedListsBySize.toArray()[1];
			def thirdList = sortedListsBySize.toArray()[2];
			def relSize1and2 = 	(secondList.listItems.size()/firstList.listItems.size()) * 100;
			def relSize1and3 = 	(thirdList.listItems.size()/firstList.listItems.size()) * 100;
			
			//circle 1
			def circle1 = [:]
			def circle1Size = 100;
			println "circle 1:" + firstList.name + "," +circle1Size + "%"
			//circle 2
			def circle2 = [:]
			def circle2Size = relSize1and2;
			println "circle 2:" + secondList.name + "," +circle2Size + "%"
			//circle 3
			def circle3 = [:]
			def circle3Size = relSize1and3;
			println "circle 3:" + thirdList.name + "," +circle3Size + "%"
			
			//when sizes are determined,calculate pct comparisons
			//1 and 2
			def compLists1and2 = []
			compLists1and2 << firstList
			compLists1and2 << secondList
			def pct1and2 = calculatePctBetween2Lists(compLists1and2)
			//1 and 3
			def compLists1and3 = []
			compLists1and3 << firstList
			compLists1and3 << thirdList
			def pct1and3 = calculatePctBetween2Lists(compLists1and3)
			//2 and 3
			def compLists2and3 = []
			compLists2and3 << secondList
			compLists2and3 << thirdList
			def pct2and3 = calculatePctBetween2Lists(compLists2and3)
			//1 and (2 and 3)
			def pct1and23 = calculatePctBetween3Lists(sortedListsBySize)
			
			
			//create valueMap and add to result
			circle1["circle1"] =  createCircleValues(firstList,circle1Size,pct1and2[1],pct1and3[1]);
			circle2["circle2"] =  createCircleValues(secondList,circle2Size,pct2and3[1],null);
			circle3["circle3"] =  createCircleValues(thirdList,circle3Size,null,null);
			//take care of all intersection result
			def allIntersections = [:]
			def allValueMap = [:]
			allValueMap["items"] = pct1and23.toArray()[0]
			allValueMap["circleInt"] = pct1and23.toArray()[1].intValue()
			allIntersections["allCircles"]  = allValueMap
			
			vennCalculations << circle1
			vennCalculations << circle2
			vennCalculations << circle3
			vennCalculations << allIntersections
			
			return vennCalculations as JSON
		}
		
		
	}
	
	def calculatePctBetween3Lists(lists){
		def items1 = lists.toArray()[0].listItems.collect{it.value}
		def items2 = lists.toArray()[1].listItems.collect{it.value}
		def items3 = lists.toArray()[2].listItems.collect{it.value}
		//get second 2
		def tmp3 = items2 as Set
		items2.retainAll( items3 )
		def tmp4 = items1 as Set
		items1.retainAll( items2 )
		def pct1and2and3 = (items1.size() / tmp4.size())*100
		println "pct of 1 and 2 and 3: " + pct1and2and3.intValue()
		return [items1,pct1and2and3]
	}
	
	def calculatePctBetween2Lists(lists){
		def items1 = lists.toArray()[0].listItems.collect{it.value}
		def items2 = lists.toArray()[1].listItems.collect{it.value}
		def tmp = items1 as Set
		
		//come back to this code...NOT being used right now
		//----------------------------------------
		def diffI1 = items1 as Set
		println "size before:" + diffI1.size()
		def diff = items1 as Set
		diffI1.removeAll(items2)
		println "size after:" + diffI1.size()
		def count = diff.size() - diffI1.size()
		println "difference:" + count
		def pct1of2 = (count/items2.size())*100
		println "pct of "+ lists.toArray()[1].name + " inside " + lists.toArray()[0].name + " = " + pct1of2
		//-----------------------------------//
		
		items1.retainAll( items2 )
		def pct1and2 = (items1.size() / tmp.size())*100
		println "pct of " + lists.toArray()[0].name +" and "+ lists.toArray()[1].name+" : " + pct1and2.intValue()
		return [items1,pct1and2]
		//return [items1,pct1of2]
	}
	
	def createCircleValues(userList,circleSize,circleInt1,circleInt2){
		def valueMap = [:]
		valueMap["name"] = userList.name
		valueMap["circleSize"] = circleSize.intValue()
		valueMap["circleInt1"] = circleInt1
		valueMap["circleInt2"] = circleInt2
		valueMap["items"] = userList.listItems.collect{it.value}
		return valueMap
	}
	
	
def gatherTags(ids){
	Set<String> unitedTags = []
	ids.each{
	      UserList list = UserList.get(it);
			if(list.tags){
				list.tags.tokenize(" ").each{ tag ->
					unitedTags << tag
				}
			}
	}
	return unitedTags as List
}


def createList(userName, listName, listItems, studies, tags) {
		def author = GDOCUser.findByLoginName(userName)
		def listDup = author.lists().find {
			it.name == listName
		}
		if(listDup) {
			return [error: "List with name $listName already exists."]
		}
		def userListInstance = new UserList()
		userListInstance.name = listName
		userListInstance.author = author
		listItems.each {
			if(it){
				userListInstance.addToListItems(new UserListItem(value:it));
			}
		}
		studies.each {
			if(it){
				def ds = StudyDataSource.findBySchemaName(it)
				userListInstance.addToStudies(ds)
			}
		}
		if(!userListInstance.hasErrors() && userListInstance.save()) {
			tags.each {
				userListInstance.addTag(it)
			}
			return [success: "UserList ${userListInstance.name} created successfully."]
		} else {
			return [error: "Error creating UserList ${userListInstance.name}."]
		}
	}
	
	/**util method that finds metadata about list items for display purposes**/
def decorateListItems(userList){
	def metadata = [:]
	//if this is a gene list, find of there are any targets associated with it's transcribed proteins
	if(userList.tags.contains('gene')){
		userList.listItems.each{ item ->
			//add target links if available
			def targetData = []
			def targetLinks = []
			targetData = drugDiscoveryService.findTargetsFromAlias(item.value)
			metadata[item.id] = [:]
			if(targetData){
				targetData.each{ target ->
					def link = "<a href='/gdoc/moleculeTarget/show/"+target.id+"'>"+target+"</a>"
					targetLinks << link
				}
				metadata[item.id]["Target Data (proteins)"] = targetLinks
			}
		}
		
	}
	return metadata
}
	
	

}