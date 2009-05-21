
class UserListTests extends GroovyTestCase {

    void testListCreation() {
		def user = new GDOCUser(loginName:"gdocUser51");
		user.save();
		def list = new UserList(name:'myNewGeneList',author:user);
		def list_items = []
		list.save();
		def savedList = UserList.findByName('myNewGeneList');
		savedList.addToListItems(new UserListItem(value:'VEGF'))
		savedList.addToListItems(new UserListItem(value:'EGFR'))
		savedList.save();
		savedList.addTag("genes")
		savedList.addTag("egfrQuery")
		
		//assertEquals 2, UserList.findByName('myNewGeneList').listItems.size()
	//	assertEquals( "genes egfrQuery", UserList.findByName('myNewGeneList').tags)
    }

	void testIntersectLists(){
		def list= ['VEGF','EGFR','BRCA1','BRCA1','ER+','ER-','FCR','HGI']
		def list2= ['VEGF','EGFR','BRCA1','SCF7', 'QSR6','HGI']
		assert list.retainAll( list2 )
		    //remove 'b' and 'z', return true because list changed
		assert list == ['VEGF','EGFR','BRCA1','BRCA1','HGI']
		println list
		def listSet = list as Set
		println listSet
	}
	
	void testUnion(){
		def list= ['VEGF','EGFR','BRCA1','BRCA1','ER+','ER-','FCR','HGI']
		def list2= ['VEGF','EGFR','BRCA1','SCF7', 'QSR6','HGI']
		list.addAll(list2)
		def union = list as Set
		println "Union: " + union
	}
	
	void testDifferenceLists(){
		def list= ['VEGF','EGFR','BRCA1','BRCA1','ER+','ER-','FCR','HGI']
		def list2= ['VEGF','EGFR','BRCA1','SCF7', 'QSR6','HGI']
		def diff = (list as Set) + list2
		def tmp = list as Set
		tmp.retainAll(list2)
		diff.removeAll(tmp)
		println "Difference: " + diff
		
	}
}
