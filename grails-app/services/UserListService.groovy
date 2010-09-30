import grails.converters.*

class UserListService{

def securityService
def drugDiscoveryService
	
	def getPaginatedLists(filter,sharedIds,offset,userId){
		def pagedLists = []
		if(filter == "all"){
			pagedLists = getAllLists(sharedIds,offset,userId)
		}else if(filter == "hideShared"){
			pagedLists = getUsersLists(offset,userId)
		}else{
			pagedLists = getUsersListsByTimePeriod(filter,offset,userId)
		}
		return pagedLists
	}
	
	def deleteList(listId){
        def userListInstance = UserList.get(listId)
        if(userListInstance) {
            userListInstance.delete(flush:true)
			log.debug "deleted " + userListInstance
        }
        else {
            log.debug "did not delete list $listId"
        }
    }
	
	def getUsersLists(offset,user){
		def pagedLists = [:]
		def results = []
		def count = 0
		String listHQL = "SELECT distinct list FROM UserList list JOIN list.author author " + 
		"WHERE author.loginName = :loginName " +
		"ORDER BY list.dateCreated desc"
		results = UserList.executeQuery(listHQL,[loginName:user, max:10, offset:offset])
		pagedLists["results"] = results
		String listHQL2 = "SELECT count(distinct list.id) FROM UserList list JOIN list.author author " + 
		"WHERE author.loginName = :loginName " 
		count = UserList.executeQuery(listHQL2,[loginName:user])
		pagedLists["count"] = count
		/**def c = UserList.createQuery()
		pagedLists = c.listDistinct
			{
				eq("author", user)
				order("dateCreated", "desc")
			}**/
		return pagedLists
	}
	
	def getAllListIds(sharedIds,userId){
		def listIds = []
		def listHQL
		if(sharedIds){
			def ids =[]
			sharedIds.each{
				ids << new Long(it)
			}
			listHQL = "SELECT distinct list.id FROM UserList list JOIN list.author author " + 
			"WHERE author.loginName = :loginName OR list.id IN (:ids) "
			listIds = UserList.executeQuery(listHQL,[loginName:userId, ids:ids])
		}else{
			listHQL = "SELECT distinct list.id FROM UserList list JOIN list.author author " + 
			"WHERE author.loginName = :loginName "
			listIds = UserList.executeQuery(listHQL,[loginName:userId])
		}
		log.debug "got list ids $listIds"
		return listIds
	}
	
	def getAllLists(sharedIds,offset,user){
		def pagedLists = [:]
		def results = []
		def count = 0
		if(sharedIds){
			def ids =[]
			sharedIds.each{
				ids << new Long(it)
			}
			String listHQL = "SELECT distinct list FROM UserList list JOIN list.author author " + 
			"WHERE author.loginName = :loginName OR list.id IN (:ids) " +
			"ORDER BY list.dateCreated desc"
			results = UserList.executeQuery(listHQL,[loginName:user, ids:ids, max:10, offset:offset])
			pagedLists["results"] = results
			String listHQL2 = "SELECT count(distinct list.id) FROM UserList list JOIN list.author author " + 
			"WHERE author.loginName = :loginName OR list.id IN (:ids) "
			count = UserList.executeQuery(listHQL2,[loginName:user,ids:ids])
			pagedLists["count"] = count
			log.debug "all lists -> $pagedLists as Paged set"
		}else{
			String listHQL = "SELECT distinct list FROM UserList list JOIN list.author author " + 
			"WHERE author.loginName = :loginName " +
			"ORDER BY list.dateCreated desc"
			results = UserList.executeQuery(listHQL,[loginName:user, max:10, offset:offset])
			pagedLists["results"] = results
			String listHQL2 = "SELECT count(distinct list.id) FROM UserList list JOIN list.author author " + 
			"WHERE author.loginName = :loginName "
			count = UserList.executeQuery(listHQL2,[loginName:user])
			pagedLists["count"] = count
			log.debug "all lists -> $pagedLists as Paged set"
		}
		return pagedLists
	}
	
	def getAllListsNoPagination(userId,sharedIds){
		def user = GDOCUser.findByLoginName(userId)
		def pagedLists = []
		def pagedListsMaps = []
		if(sharedIds){
			def ids =[]
			sharedIds.each{
				ids << new Long(it)
			}
			pagedLists = UserList.createCriteria().list()
				{
					projections{
						property('id')
						property('name')
					}
					and{
						'order'("dateCreated", "desc")
					}
					or {
						eq("author", user)
						'in'('id',ids)
					}
				}
		}
		else {
			pagedLists = UserList.createCriteria().list()
				{
					projections{
						property('id')
						property('name')
					}
					and{
						'order'("dateCreated", "desc")
					}
					or {
						eq("author", user)
					}
				}
		}
		pagedLists.each{
			def pagedListsMap = [:]
			pagedListsMap["id"] = it[0]
			pagedListsMap["name"] = it[1]
			pagedListsMaps << pagedListsMap
		}
		log.debug "all lists snapsot -> $pagedListsMaps "
		return pagedListsMaps
	}
	
	def getUsersListsByTimePeriod(timePeriod,offset,user) {
		def pagedLists = [:]
		def results = []
		def count = 0
		def now = new Date()
		def tp = Integer.parseInt(timePeriod)
		def range = now-tp
		String listHQL = "SELECT distinct list FROM UserList list JOIN list.author author " + 
		"WHERE author.loginName = :loginName " +
		"AND list.dateCreated BETWEEN :range and :now "
		"ORDER BY list.dateCreated desc"
		results = UserList.executeQuery(listHQL,[loginName:user, now:now, range:range, max:10, offset:offset])
		pagedLists["results"] = results
		String listHQL2 = "SELECT count(distinct list.id) FROM UserList list JOIN list.author author " + 
		"WHERE author.loginName = :loginName " +
		"AND list.dateCreated BETWEEN :range and :now "
		count = UserList.executeQuery(listHQL2,[loginName:user, now:now, range:range])
		pagedLists["count"] = count

		log.debug "user lists only over past $timePeriod days-> $pagedLists as Paged set"
		return pagedLists
	}
	
	def getListsByTag(tag,sharedIds, userName){
		def ids = []
		def taggedLists = []
		if(sharedIds){
			sharedIds.each{
				ids << new Long(it)
			}
		}
		String listHQL
		if(ids){
			listHQL = "SELECT distinct list FROM UserList list,TagLink tagLink JOIN list.author author " + 
			"WHERE list.id = tagLink.tagRef	AND tagLink.type = 'userList' " +
			"AND (author.loginName = :loginName " +
			"OR list.id IN (:ids)) " + 
			"AND tagLink.tag.name = :tag " +
			"ORDER BY list.name"
			taggedLists = UserList.executeQuery(listHQL, [tag: tag, ids:ids, loginName: userName])
		}
		else{
			listHQL = "SELECT distinct list FROM UserList list,TagLink tagLink JOIN list.author author " + 
			"WHERE list.id = tagLink.tagRef	AND tagLink.type = 'userList' " +
			"AND tagLink.tag.name = :tag " +
			"AND author.loginName = :loginName " +
			"ORDER BY list.name"
			taggedLists = UserList.executeQuery(listHQL, [tag: tag, loginName: userName])
		}
		return taggedLists
		
	}
	
	def getListsByTagAndStudy(tag,study,sharedIds,userName){
		def sids =[]
		def ids = []
		def patientLists = [] 
		sids << study
		if(sharedIds){
			sharedIds.each{
				ids << new Long(it)
			}
		}
		String listHQL
		if(ids){
			listHQL = "SELECT distinct list FROM UserList list,TagLink tagLink JOIN list.studies studies " + 
			"WHERE list.id = tagLink.tagRef AND tagLink.type = 'userList' " +
			"AND tagLink.tag.name = :tag " +
			"AND (list.author.loginName = :loginName " +
			"OR list.id IN (:ids)) " + 
			"AND studies IN (:sids) " + 
			"ORDER BY list.name"
			patientLists = UserList.executeQuery(listHQL, [tag: tag, loginName: userName, ids:ids, sids:sids])
		}else{
			listHQL = "SELECT distinct list FROM UserList list,TagLink tagLink JOIN list.studies studies " + 
			"WHERE list.id = tagLink.tagRef	AND tagLink.type = 'userList' " +
			"AND tagLink.tag.name = :tag " +
			"AND list.author.loginName = :loginName " +
			"AND studies IN (:sids) " + 
			"ORDER BY list.name"
			patientLists = UserList.executeQuery(listHQL, [tag: tag, loginName: userName, sids:sids])
		}
		return patientLists
		
	}
	
	def getTempListIds(userId) {
		String findByTagHQL = """
		   SELECT DISTINCT list.id
		   FROM UserList list, TagLink tagLink
		   JOIN list.author author
		   WHERE list.id = tagLink.tagRef
		   AND author.loginName = :loginName
		   AND tagLink.type = 'userList'
		   AND tagLink.tag.name = :tag
		"""
		
		def listIds = UserList.executeQuery(findByTagHQL, [tag: Constants.TEMPORARY, loginName: userId])
		return listIds	
	}
	
	def getSharedListIds(userId){
		log.debug "get $userId shared lists"
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
				list.tags.each{ tag ->
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
		Set unitedStudies = []
		def vids = []
		if(ids.metaClass.respondsTo(ids, "max")) {
			ids.each{
				  log.debug it + " has been sent"
			      UserList list = UserList.get(it);
				  lists.add(list);
				  if(list.studies){
					list.studies.each{
						unitedStudies << it.schemaName
					}
				  }
			}
		}
		else{
			UserList list = UserList.get(ids);
			lists.add(list);
			if(list.studies){
				list.studies.each{
					unitedStudies << it.schemaName
				}
			 }
		}
		
		
		
		def sortedListsBySize = lists.sort{ l ->
			if(l.listItems){
				-l.listItems.size()
			}
		}
		
		sortedListsBySize.each{
			if(it.listItems){
				log.debug it.name + ": " + it.listItems.size()
			}else{
				log.debug it.name + ": has no list items"
				return null;
			}
		}
		
		
		//if only 2 lists
		if(sortedListsBySize.size() == 2){
			log.debug "venn diagram of 2 lists"
			def firstList = sortedListsBySize.toArray()[0];
			def secondList = sortedListsBySize.toArray()[1];
			def relSize1and2 = 	(secondList.listItems.size()/firstList.listItems.size()) * 100;
			
			//circle 1
			def circle1 = [:]
			def circle1Size = 100;
			log.debug "circle 1:" + firstList.name + "," +circle1Size + "%"
			//circle 2
			def circle2 = [:]
			def circle2Size = relSize1and2;
			log.debug "circle 2:" + secondList.name + "," +circle2Size + "%"
			
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
			allValueMap["studies"] = unitedStudies.join(",")
			allValueMap["circleInt"] = pct1and2.get(1)
			log.debug pct1and2.get(1).class
			allIntersections["allCircles"]  = allValueMap
			
			vennCalculations << circle1
			vennCalculations << circle2
			vennCalculations << allIntersections
			log.debug "return venn Calculations"
			return vennCalculations as JSON
		}
		//if 3 lists
		else if(sortedListsBySize.size() == 3){
			log.debug "venn diagram of 3 lists"
			def firstList = sortedListsBySize.toArray()[0];
			def secondList = sortedListsBySize.toArray()[1];
			def thirdList = sortedListsBySize.toArray()[2];
			def relSize1and2 = 	(secondList.listItems.size()/firstList.listItems.size()) * 100;
			def relSize1and3 = 	(thirdList.listItems.size()/firstList.listItems.size()) * 100;
			
			//circle 1
			def circle1 = [:]
			def circle1Size = 100;
			log.debug "circle 1:" + firstList.name + "," +circle1Size + "%"
			//circle 2
			def circle2 = [:]
			def circle2Size = relSize1and2;
			log.debug "circle 2:" + secondList.name + "," +circle2Size + "%"
			//circle 3
			def circle3 = [:]
			def circle3Size = relSize1and3;
			log.debug "circle 3:" + thirdList.name + "," +circle3Size + "%"
			
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
		log.debug "pct of 1 and 2 and 3: " + pct1and2and3.intValue()
		return [items1,pct1and2and3]
	}
	
	def calculatePctBetween2Lists(lists){
		def items1 = lists.toArray()[0].listItems.collect{it.value}
		def items2 = lists.toArray()[1].listItems.collect{it.value}
		def tmp = items1 as Set
		
		//come back to this code...NOT being used right now
		//----------------------------------------
		def diffI1 = items1 as Set
		log.debug "size before:" + diffI1.size()
		def diff = items1 as Set
		diffI1.removeAll(items2)
		log.debug "size after:" + diffI1.size()
		def count = diff.size() - diffI1.size()
		log.debug "difference:" + count
		def pct1of2 = (count/items2.size())*100
		log.debug "pct of "+ lists.toArray()[1].name + " inside " + lists.toArray()[0].name + " = " + pct1of2
		//-----------------------------------//
		
		items1.retainAll( items2 )
		def pct1and2 = (items1.size() / tmp.size())*100
		log.debug "pct of " + lists.toArray()[0].name +" and "+ lists.toArray()[1].name+" : " + pct1and2.intValue()
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
				list.tags.each{ tag ->
					unitedTags << tag
				}
			}
	}
	return unitedTags as List
}


def createList(userName, listName, listItems, studies, tags) {
		def author = GDOCUser.findByLoginName(userName)
		def listDup = author.lists.find {
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
	
	
	def createAndReturnList(userName, listName, listItems, studies, tags) {
			def author = GDOCUser.findByLoginName(userName)
			def listDup = author.lists.find {
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
					//log.debug "add tag, $it"
					userListInstance.addTag(it)
				}
				log.debug "UserList ${userListInstance.name} created successfully."
				return userListInstance
			} else {
				log.debug "Error creating UserList ${userListInstance.name}."
				return null
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
	
def listIsTemporary(listName,author){
	log.debug "find if $listName is temporary"
	def compList = UserList.findByNameAndAuthor(listName,author)
	if(compList && compList.tags?.contains(Constants.TEMPORARY)){
		return true
	}
	else{
		return false
	}
	return false
}	

}