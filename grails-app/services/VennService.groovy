import grails.converters.*

class VennService{
	
def organizeVennLists(ids){
	List<UserList> lists = new ArrayList<UserList>();
	Set unitedStudies = []
	def vids = []
	if(ids.metaClass.respondsTo(ids, "max")) {
		ids.each{
			  log.debug it + " has been sent"
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
			log.debug it.name + ": " + it.listItems.size()
		}else{
			log.debug it.name + ": has no list items"
			return null;
		}
	}
	
	return sortedListsBySize;
}

def vennDiagram(name,author,ids){
	//need to calculate relative size by comparison
	def vennCalculations = [:]
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
		vennCalculations = createIntersection2ListDictionary(firstList,secondList)
		
		/**
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
		vennCalculations << allIntersections**/
		log.debug "return venn Calculations"
		return vennCalculations as JSON
	}
	//if 3 lists
	else if(sortedListsBySize.size() == 3){
		log.debug "venn diagram of 3 lists"
		def firstList = sortedListsBySize.toArray()[0];
		def secondList = sortedListsBySize.toArray()[1];
		def thirdList = sortedListsBySize.toArray()[2];
		vennCalculations = createIntersection3ListDictionary(firstList,secondList,thirdList)
		
		/**
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
		vennCalculations << allIntersections**/
		
		return vennCalculations as JSON
	}
	//if 3 lists
	else if(sortedListsBySize.size() == 4){
		log.debug "venn diagram of 3 lists"
		def firstList = sortedListsBySize.toArray()[0];
		def secondList = sortedListsBySize.toArray()[1];
		def thirdList = sortedListsBySize.toArray()[2];
		def fourthList = sortedListsBySize.toArray()[2];
		vennCalculations = createIntersection4ListDictionary(firstList,secondList,thirdList,fourthList)
		
		return vennCalculations as JSON
	}
	
}


def createIntersection2ListDictionary(lists){
	def dictionary = [:]
	def vennData = [:]
	def graphData = [:]
	def listA = lists.toArray()[0].listItems.collect{it.value}
	def a = lists.toArray()[0]
	//log.debug lists.toArray()[0].name + " has $listA.size items"
	def listB = lists.toArray()[1].listItems.collect{it.value}
	def b = lists.toArray()[1]
	//log.debug lists.toArray()[1].name + " has $listB.size items"
	def listAOnly = listA as Set
	dictionary[a.name] = listAOnly
	def listBOnly = listB as Set
	dictionary[b.name] = listBOnly
	def listAB = []
	dictionary[a.name+b.name] = listAB
	dictionary["names"] = []
	dictionary["names"] << a.name
	dictionary["names"] << b.name
	dictionary["names"] << a.name+b.name
	
	if(listAOnly.removeAll(listB)){
		dictionary[a.name] = listAOnly as List
		//log.debug "$a currently has size of "+listAOnly.size()
	}
	if(listBOnly.removeAll(listA)){
		dictionary[b.name] = listBOnly as List
		//log.debug "$b currently has size of "+listBOnly.size()
		
	}
	
	if(listA.retainAll(listB)){
		if(listA.size() > 0){
			listAB = listA
			//log.debug "found intersection beetween $a.name and $b.name"
			//log.debug "2 lists now have intersection data of $listAB.size"
			dictionary[a.name+b.name] = listAB
		}else{
			dictionary[a.name+b.name] = []
		}
		def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,null,null)
		vennData = vennInfo[0]
		graphData = vennInfo[1]
		
	}
	else{
		if(listA.size() > 0){
			listAB = listA
			//log.debug "found intersection beetween $a.name and $b.name"
			//log.debug "2 lists now have intersection data of $listAB.size"
			dictionary[a.name+b.name] = listAB
			def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,null,null)
			vennData = vennInfo[0]
			graphData = vennInfo[1]
		}
		else{
			log.debug "found NO intersection beetween $a.name and $b.name"
			dictionary[a.name+b.name] = []
		}
		def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,null,null)
		vennData = vennInfo[0]
		graphData = vennInfo[1]
	}
	def vennContainer = new VennContainer(dictionary:dictionary, vennData:vennData as JSON, graphData: graphData as JSON)
	return vennContainer
}

def createIntersection3ListDictionary(lists){
	def dictionary = [:]
	def vennData = [:]
	def graphData = [:]
	//get intersection of a&b, a&c, & b&c
	def a = lists.toArray()[0]
	def b = lists.toArray()[1]
	def c = lists.toArray()[2]
	
	def a_bMap = createIntersection2ListDictionary([a,b])
	def a_cMap = createIntersection2ListDictionary([a,c])
	def b_cMap = createIntersection2ListDictionary([b,c])
	log.debug a_bMap
	dictionary[a.name+b.name] = a_bMap.dictionary[a.name+b.name]
	log.debug a_cMap
	dictionary[a.name+c.name] = a_cMap.dictionary[a.name+c.name]
	log.debug b_cMap
	dictionary[b.name+c.name] = b_cMap.dictionary[b.name+c.name]
	
	def listA = a.listItems.collect{it.value}
	def listB = b.listItems.collect{it.value}
	def listC = c.listItems.collect{it.value}

	def listAOnly = listA as Set
	dictionary[a.name] = listAOnly
	def listBOnly = listB as Set
	dictionary[b.name] = listBOnly
	def listCOnly = listC as Set
	dictionary[c.name] = listCOnly
	def listABC = []
	dictionary[a.name+b.name+c.name] = listABC
	dictionary["names"] = []
	dictionary["names"] << a.name
	dictionary["names"] << b.name
	dictionary["names"] << a.name+b.name
	dictionary["names"] << c.name
	dictionary["names"] << a.name+c.name
	dictionary["names"] << b.name+c.name
	dictionary["names"] << a.name+b.name+c.name
	
	
	//take care of only lists
	//log.debug "$a.name is $listAOnly"
	if(listAOnly.removeAll(a_bMap.dictionary[a.name+b.name])){
		//log.debug "$a.name now has size of " + listAOnly.size() + ", removed " + a_bMap["intersection"] + " or " + dictionary["a_b_intersection"]
		dictionary["A"] = listAOnly
	}
	//log.debug "$a.name is $listAOnly"
	if(listAOnly.removeAll(a_cMap.dictionary[a.name+c.name])){
		//log.debug "$a.name now has size of " + listAOnly.size() + ", removed  ac intersect"
		dictionary["A"] = listAOnly
	}
	//log.debug "$a.name is $listAOnly"
	//log.debug "$b.name is $listBOnly"
	if(listBOnly.removeAll(a_bMap.dictionary[a.name+b.name])){
		//log.debug "$b.name now has size of " + listBOnly.size() + ", removed ba intersect"
		dictionary["B"] = listBOnly
	}
	//log.debug "$b.name is $listBOnly"
	if(listBOnly.removeAll(b_cMap.dictionary[b.name+c.name])){
		//log.debug "$b.name now has size of " + listBOnly.size() + ", removed  bc intersect"
		dictionary["B"] = listBOnly
	}
	//log.debug "$b.name is $listBOnly"
	//log.debug "$c.name is $listCOnly"
	if(listCOnly.removeAll(a_cMap.dictionary[a.name+c.name])){
		//log.debug "$c.name now has size of " + listCOnly.size() + ", removed ac intersect"
		dictionary["C"] = listCOnly
	}
	//log.debug "$c.name is $listCOnly"
	if(listCOnly.removeAll(b_cMap.dictionary[b.name+c.name])){
		//log.debug "$c.name now has size of " + listCOnly.size() + ", removed  bc intersect"
		dictionary["C"] = listCOnly
	}
	//log.debug "$c.name is $listCOnly"

	
	
	if(dictionary[a.name+b.name].size() > 0){
		def listAB = []
		dictionary[a.name+b.name].each{
			listAB << it
		}
		if(listAB.retainAll(listC)){
			if(listAB.size() > 0){
				listABC = listAB
				//log.debug "found intersection beetween $a.name and $b.name and $c.name"
				//log.debug "3 lists now have intersection data of $listABC.size()"
				dictionary[a.name+b.name+c.name] = listABC
				def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,c,null)
				vennData = vennInfo[0]
				graphData = vennInfo[1]
			}else{
				log.debug "no intersection beetween $a.name and $b.name and $c.name, zero in common"
				dictionary[a.name+b.name+c.name] = []
				def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,c,null)
				vennData = vennInfo[0]
				graphData = vennInfo[1]
			}
		}
		else{
			if(listAB.size() > 0){
				listABC = listAB
				//log.debug "found intersection beetween $a.name and $b.name and $c.name, but no change in list"
				//log.debug "3 lists now have intersection data of $listABC.size()"
				dictionary[a.name+b.name+c.name] = listABC
				def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,c,null)
				vennData = vennInfo[0]
				graphData = vennInfo[1]
			}else{
				log.debug "no intersection beetween $a.name and $b.name and $c.name"
				dictionary[a.name+b.name+c.name] = []
				def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,c,null)
				vennData = vennInfo[0]
				graphData = vennInfo[1]
			}
		}
	}
	else{
		log.debug "found NO intersection beetween beetween $a.name and $b.name, so ne need to check $c.name"
		dictionary[a.name+b.name+c.name] = []
		def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,c,null)
		vennData = vennInfo[0]
		graphData = vennInfo[1]
	}
	def vennContainer = new VennContainer(dictionary:dictionary, vennData:vennData as JSON, graphData: graphData as JSON)
	return vennContainer
}

def createIntersection4ListDictionary(lists){
	def dictionary = [:]
	def vennData = [:]
	def graphData = [:]
	//get intersection of a&b, a&c, a&d, b&c, b&d, c&d 
	def a = lists.toArray()[0]
	def b = lists.toArray()[1]
	def c = lists.toArray()[2]
	def d = lists.toArray()[3]
	
	//get 2 relations
	def a_bMap = createIntersection2ListDictionary([a,b])
	def a_cMap = createIntersection2ListDictionary([a,c])
	def a_dMap = createIntersection2ListDictionary([a,d])
	def b_cMap = createIntersection2ListDictionary([b,c])
	def b_dMap = createIntersection2ListDictionary([b,d])
	def c_dMap = createIntersection2ListDictionary([c,d])
	
	//get all 3 relations
	def a_b_cMap = createIntersection3ListDictionary([a,b,c])
	def a_b_dMap = createIntersection3ListDictionary([a,b,d])
	def a_c_dMap = createIntersection3ListDictionary([a,c,d])
	def b_c_dMap = createIntersection3ListDictionary([b,c,d])
	
	//get all 4 relations
	def listABCD = []
	dictionary[a.name+b.name+c.name+d.name] = listABCD
	
	dictionary["names"] = []
	dictionary["names"] << a.name
	dictionary["names"] << b.name
	dictionary["names"] << a.name+b.name
	dictionary["names"] << c.name
	dictionary["names"] << a.name+c.name
	dictionary["names"] << b.name+c.name
	dictionary["names"] << a.name+b.name+c.name
	dictionary["names"] << d.name
	dictionary["names"] << a.name+d.name
	dictionary["names"] << b.name+d.name
	dictionary["names"] << c.name+d.name
	dictionary["names"] << a.name+b.name+d.name
	dictionary["names"] << a.name+c.name+d.name
	dictionary["names"] << b.name+c.name+d.name
	dictionary["names"] << a.name+b.name+c.name+d.name
	
	
	log.debug a_bMap
	dictionary[a.name+b.name] = a_bMap.dictionary[a.name+b.name]
	log.debug a_cMap
	dictionary[a.name+c.name] = a_cMap.dictionary[a.name+c.name]
	log.debug a_dMap
	dictionary[a.name+d.name] = a_dMap.dictionary[a.name+d.name]
	
	log.debug b_cMap
	dictionary[b.name+c.name] = b_cMap.dictionary[b.name+c.name]
	log.debug b_dMap
	dictionary[b.name+d.name] = b_dMap.dictionary[b.name+d.name]
	
	log.debug c_dMap
	dictionary[c.name+d.name] = c_dMap.dictionary[c.name+d.name]
	
	log.debug a_b_cMap
	dictionary[a.name+b.name+c.name]= a_b_cMap.dictionary[a.name+b.name+c.name]
	log.debug a_b_dMap
	dictionary[a.name+b.name+d.name]= a_b_dMap.dictionary[a.name+b.name+d.name]
	log.debug a_c_dMap
	dictionary[a.name+c.name+d.name]= a_c_dMap.dictionary[a.name+c.name+d.name]
	log.debug b_c_dMap
	dictionary[b.name+c.name+d.name]= b_c_dMap.dictionary[b.name+c.name+d.name]

	
	def listA = []
	listA = a.listItems.collect{it.value}
	def listB = []
	listB = b.listItems.collect{it.value}
	def listC = []
	listC = c.listItems.collect{it.value}
	def listD = []
	listD = d.listItems.collect{it.value}

	def listAOnly = listA as Set
	dictionary[a.name] = listAOnly
	def listBOnly = listB as Set
	dictionary[b.name] = listBOnly
	def listCOnly = listC as Set
	dictionary[c.name] = listCOnly
	def listDOnly = listD as Set
	dictionary[d.name] = listDOnly
	
	
	//take care of only lists
	//log.debug "$a.name is $listAOnly"
	if(listAOnly.removeAll(a_bMap.dictionary[a.name+b.name])){
		//log.debug "$a.name now has size of " + listAOnly.size() + ", removed ab intersect"
		dictionary[a.name] = listAOnly
	}
	//log.debug "$a.name is $listAOnly"
	if(listAOnly.removeAll(a_cMap.dictionary[a.name+c.name])){
		//log.debug "$a.name now has size of " + listAOnly.size() + ", removed  ac intersect"
		dictionary[a.name] = listAOnly
	}
	if(listAOnly.removeAll(a_dMap.dictionary[a.name+d.name])){
		log.debug "$a.name now has size of " + listAOnly.size() + ", removed  ad intersect"
		dictionary[a.name] = listAOnly
	}
	//log.debug "$a.name is $listAOnly"
	//log.debug "$b.name is $listBOnly"
	if(listBOnly.removeAll(a_bMap.dictionary[a.name+b.name])){
		//log.debug "$b.name now has size of " + listBOnly.size() + ", removed ba intersect"
		dictionary[b.name] = listBOnly
	}
	//log.debug "$b.name is $listBOnly"
	if(listBOnly.removeAll(b_cMap.dictionary[b.name+c.name])){
		//log.debug "$b.name now has size of " + listBOnly.size() + ", removed  bc intersect"
		dictionary[b.name] = listBOnly
	}
	if(listBOnly.removeAll(b_dMap.dictionary[b.name+d.name])){
		//log.debug "$b.name now has size of " + listBOnly.size() + ", removed  bd intersect"
		dictionary[b.name] = listBOnly
	}
	//log.debug "$b.name is $listBOnly"
	//log.debug "$c.name is $listCOnly"
	if(listCOnly.removeAll(a_cMap.dictionary[a.name+c.name])){
		//log.debug "$c.name now has size of " + listCOnly.size() + ", removed ac intersect"
		dictionary[c.name] = listCOnly
	}
	//log.debug "$c.name is $listCOnly"
	if(listCOnly.removeAll(b_cMap.dictionary[b.name+c.name])){
		//log.debug "$c.name now has size of " + listCOnly.size() + ", removed  bc intersect"
		dictionary[c.name] = listCOnly
	}
	if(listCOnly.removeAll(c_dMap.dictionary[c.name+d.name])){
		//log.debug "$c.name now has size of " + listCOnly.size() + ", removed  cd intersect"
		dictionary[c.name] = listCOnly
	}
	//log.debug "$c.name is $listCOnly"
	if(listDOnly.removeAll(a_dMap.dictionary[a.name+d.name])){
		//log.debug "$d.name now has size of " + listDOnly.size() + ", removed  ad intersect"
		dictionary[d.name] = listDOnly
	}
	if(listDOnly.removeAll(b_dMap.dictionary[b.name+d.name])){
		//log.debug "$d.name now has size of " + listDOnly.size() + ", removed  bd intersect"
		dictionary[d.name] = listDOnly
	}
	if(listDOnly.removeAll(c_dMap.dictionary[c.name+d.name])){
		//log.debug "$d.name now has size of " + listDOnly.size() + ", removed  cd intersect"
		dictionary[d.name] = listDOnly
	}
	//log.debug "$c.name is $listCOnly"

	
	
	if(a_b_cMap.dictionary[a.name+b.name+c.name] && a_b_cMap.dictionary[a.name+b.name+c.name].size() > 0){
		log.debug "found int for a_b_cMap "+ a_b_cMap.dictionary[a.name+b.name+c.name]
		def listABC = []
		a_b_cMap.dictionary[a.name+b.name+c.name].each{
			listABC << it
		}
		if(listABC.retainAll(listD)){
			if(listABC.size() > 0){
				listABCD = listABC
				//log.debug "found intersection beetween $a.name and $b.name and $c.name and $d.name"
				//log.debug "4 lists now have intersection data of $listABCD.size()"
				dictionary[a.name+b.name+c.name+d.name] = listABCD
				//finalize for venn display
				def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,c,d)
				vennData = vennInfo[0]
				graphData = vennInfo[1]
			}else{
				log.debug "no intersection beetween $a.name and $b.name and $c.name and $d.name, zero in common"
				dictionary[a.name+b.name+c.name+d.name] = []
				def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,c,d)
				vennData = vennInfo[0]
				graphData = vennInfo[1]
			}
		}
		else{
			if(listABC.size() > 0){
				listABCD = listABC
				//log.debug "found intersection beetween $a.name and $b.name and $c.name and $d.name, but no change in list"
				//log.debug "4 lists now have intersection data of $listABCD.size()"
				dictionary[a.name+b.name+c.name+d.name] = listABCD
				def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,c,d)
				vennData = vennInfo[0]
				log.debug vennData
				graphData = vennInfo[1]
				log.debug graphData
			}else{
				log.debug "no intersection beetween $a.name and $b.name and $c.name and $d.name"
				dictionary[a.name+b.name+c.name+d.name] = []
				def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,c,d)
				vennData = vennInfo[0]
				graphData = vennInfo[1]
			}
		}
	}
	else{
		log.debug "no found NO intersection beetween beetween $a.name and $b.name and $c.name, so ne need to check $d.name"
		dictionary[a.name+b.name+c.name+d.name] = []
		def vennInfo = finalizeGroupsForVennDisplay(dictionary,a,b,c,d)
		vennData = vennInfo[0]
		graphData = vennInfo[1]
	}
	def vennContainer = new VennContainer(dictionary:dictionary, vennData:vennData as JSON, graphData: graphData as JSON)
	return vennContainer
}

def finalizeGroupsForVennDisplay(dictionary,a,b,c,d){
	def vennData = [:]
	def graphData = [:]
	graphData["graphType"] = "Venn"
	graphData["vennGroups"] = 2
	graphData["autoExtend"] = true
	vennData["venn"] = [:]
	def pairings = vennData["venn"]
	pairings["data"] = [:]
	pairings["legend"] = [:]
	
	def data = pairings["data"]
	data[a.name] = dictionary[a.name].size()
	data[b.name] = dictionary[b.name].size()
	data[a.name+b.name] = dictionary[a.name+b.name].size()
	if(c){
		data[c.name] = dictionary[c.name].size()
		data[a.name+c.name] = dictionary[a.name+c.name].size()
		data[b.name+c.name] = dictionary[b.name+c.name].size()
		data[a.name+b.name+c.name] = dictionary[a.name+b.name+c.name].size()
		if(d){
			data[d.name] = dictionary[d.name].size()
			data[c.name+d.name] = dictionary[c.name+d.name].size()
			data[a.name+c.name+d.name] = dictionary[a.name+c.name+d.name].size()
			data[a.name+d.name] = dictionary[a.name+d.name].size()
			data[b.name+d.name] = dictionary[b.name+d.name].size()
			data[a.name+b.name+d.name] = dictionary[a.name+b.name+d.name].size()
			data[b.name+c.name+d.name] = dictionary[b.name+c.name+d.name].size()
			data[a.name+b.name+c.name+d.name] = dictionary[a.name+b.name+c.name+d.name].size()
		}
	}
	def legend = pairings["legend"]
	legend[a.name] = a.listItems.size() +" items"
	legend[b.name] = b.listItems.size() +" items"
	if(c){
		legend[c.name] = c.listItems.size() +" items"
		graphData["vennGroups"] = 3
	}
	if(d){
		legend[d.name] = d.listItems.size() +" items"
		graphData["vennGroups"] = 4
	}
	log.debug vennData
    return [vennData,graphData]
}

//-------------------------BELOW THIS LINE SHOULD BE REMOVED ONCE REFACTOR IS COMPLETE-----------------------------------------//




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
}
