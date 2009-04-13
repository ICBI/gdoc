
class UserListTests extends GroovyTestCase {

    void testListCreation() {
		def user = new GDOCUser(username:"gdocUser");
		user.save();
		def list = new UserList(name:'myNewGeneList',author:user);
		def list_items = []
		list.save();
		def savedList = UserList.findByName('myNewGeneList');
		savedList.addToList_items(new UserListItem(value:'VEGF'))
		savedList.addToList_items(new UserListItem(value:'EGFR'))
		savedList.save();
		savedList.addTag("genes")
		savedList.addTag("egfrQuery")
		
		assertEquals 2, UserList.findByName('myNewGeneList').list_items.size()
		assertEquals( "genes egfrQuery", UserList.findByName('myNewGeneList').tags)
    }
}
