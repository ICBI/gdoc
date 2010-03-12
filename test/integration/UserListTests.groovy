

class UserListTests extends GroovyTestCase {
	
	void testNothing() {
		
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
		
	}
	
	void testVenn(){
		def listComp1= ['VEGF','EB','Ez-','FCR','HGI']
		println "list one: " + listComp1
		def listComp2 = []
		listComp2.addAll(listComp1)
		def list2= ['VEGF','EB','SOM1','XYZ','UTY']
		println "list two: " + list2
		def list3= ['VEGF','EGFR','ER+','5t5t']
		println "list three: " + list3
		def last = []
		last.addAll(listComp1)
		
		//compare 1 and 2
		def tmp = listComp1 as Set
		listComp1.retainAll( list2 )
		def pct1and2 = (listComp1.size() / tmp.size())*100
		println "pct of 1 and 2: " + pct1and2.intValue()
		//compare 1 and 3
		def tmp2 = listComp2 as Set
		listComp2.retainAll( list3 )
		def pct1and3 = (listComp2.size() / tmp2.size())*100
		println "pct of 1 and 3: " + pct1and3.intValue()
		//compare 2 and 3
		def tmp3 = list2 as Set
		list2.retainAll( list3 )
		println "retained list2= " + list2.size() + " untouched list2 ="+ tmp3.size()
		def pct2and3 = (list2.size() / tmp3.size())*100
		println "pct of 2 and 3: " + pct2and3.intValue()
		//compare 1 and 2 and 3 30,25,20,10
		def tmp4 = last as Set
		last.retainAll( list2 )
		def pct1and2and3 = (last.size() / tmp4.size())*100
		println "pct of 1 and 2 and 3: " + pct1and2and3.intValue()
	}*/
}
