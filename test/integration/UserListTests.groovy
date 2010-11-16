import grails.orm.PagedResultList
import org.hibernate.Criteria
import org.hibernate.criterion.Projections

class UserListTests extends GroovyTestCase {
	
	def userListService
	def vennService
	
	void testNothing(){
		
	}
/*
	void testCreateTempUserListsForKM(){
		def templists = []
		def tempIds = ['VEGF','EGFR','BRCA1','BRCA1','ER+','ER-','FCR','HGI']
		def greater = new UserList(name:'greater')
		greater.listItems = []
		tempIds.each{
			def item = new UserListItem(value:'test',list:greater)
			greater.addToListItems(item)
			println item.value
		}
	    templists << greater
	 	templists.each{list ->
			list.listItems.each{
			 println it.value
			}
		}
	}

	void testIntersectLists(){
		def list1 = UserList.findByName("list1");
		println list1.listItems
		def list2 = UserList.findByName("list2");
		println list2.listItems
		def list3 = UserList.findByName("list3");
		println list3.listItems
		def allLists = []
		allLists.add(list1.id);
		allLists.add(list2.id);
		//allLists.add(list3.id);
		def items = new ArrayList<String>();
		List<UserList> lists = new ArrayList<UserList>();
		allLists.each{
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
					println "values " + values
		            intersectedList.retainAll(values);
		 }
		 items.clear();
		 items.addAll(intersectedList);
		def userList = new UserList(name:'newList',listItems:items,author:'kmr');
		println "Intersection list: " + intersectedList
		
		
	}
	
	void testUnion(){
		def list1 = UserList.findByName("list1");
		println list1.listItems
		def list2 = UserList.findByName("list2");
		println list2.listItems
		def list3 = UserList.findByName("list3");
		println list3.listItems
		def allLists = []
		allLists.add(list1.id);
		allLists.add(list2.id);
		def items = new ArrayList<String>();
	        allLists.each{
	            UserList list = UserList.get(it);
	            if(!list.listItems.isEmpty()){
	                items.addAll(list.listItems.collect{it.value});
	            }
	        }
	    Set<String> unitedSet = new HashSet<String>(items);
	    println "Union of lists: " + unitedSet
	}
	
	void testDifferenceLists(){
		def list1 = UserList.findByName("list1");
		def items1 = []
		list1.listItems.each{
			print it.value + ","
			items1 << it.value
		}
		def items2 = []
		def list2 = UserList.findByName("list2");
		list2.listItems.each{
			print it.value + ","
			items2 << it.value
		}
		
		
		def diff = (items1 as Set) + items2
		println "so far: " + diff
		def tmp = items1 as Set
		tmp.retainAll(items2)
		diff.removeAll(tmp)
		println "Difference: " + diff
		
	}**/
	
	void testVenn(){
		def list1Items= ['VEGF','EB','Ez-','FCR','HGI','HER2','BRCA1','GEF']
		def userlist1 = new UserList(name:"listA")
		userlist1.listItems = []
		list1Items.each{
			def item1 = new UserListItem(value:it,list:userlist1)
			userlist1.addToListItems(item1)
			//println item1.value
		}
		//a_b = 4
		//a_c = 3
		//a_d = 6
		
		def list2Items= ['VEGF','EB','SOM1','XYZ','UTY','HER2','GEF']
		def userlist2 = new UserList(name:"list_B")
		userlist2.listItems = []
		list2Items.each{
			def item2 = new UserListItem(value:it,list:userlist2)
			userlist2.addToListItems(item2)
			//println item2.value
		}
		//b_c = 2
		//b_d = 5
		
		def list3Items= ['VEGF','EGFR','ER+','5t5t','HER2','BRCA1']
		def userlist3 = new UserList(name:"listC")
		userlist3.listItems = []
		list3Items.each{
			def item3 = new UserListItem(value:it,list:userlist3)
			userlist3.addToListItems(item3)
			//println item3.value
		}
		
		//c_d= 5
		
		def list4Items= ['VEGF','EB','ER+','5t5t','HER2','BRCA1','SOM1','RED4','GEF','HER2','FCR']
		def userlist4 = new UserList(name:"listD")
		userlist4.listItems = []
		list4Items.each{
			def item4 = new UserListItem(value:it,list:userlist4)
			userlist4.addToListItems(item4)
			//println item3.value
		}
		
		//a_b_c=2
		//a_b_c_d = 2
		
		def allLists = []
		allLists << userlist1
		allLists << userlist2
		//allLists << userlist3
		//allLists << userlist4
		//get intersection of a&b&c
		def vennContainer = vennService.createIntersection2ListDictionary(allLists)
		println vennContainer.dictionary
		println vennContainer.vennData
		println vennContainer.graphData
		
	}
	/**
	void testRetain(){
		def list3Items= ['VEGF','EGFR']
		def list4Items= ['VEGF1','EGFR1','EB','ER+','5t5t','HER2','BRCA1','SOM1','RED4','GEF','HER2','FCR']
		if(list3Items.retainAll(list4Items)){
			if(list3Items.size() > 0){
				println "found intersection beetween lists"
			}else{
				println "no intersection beetween lists, it was set to 0"
			}
		}
		else{
			if(list3Items.size() > 0){
				println "found intersection beetweenlists, even though no change"
			}else{
				println "no intersection, no change beetween lists"
			}
		}
	}**/
}
