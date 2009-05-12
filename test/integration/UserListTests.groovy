
class UserListTests extends GroovyTestCase {

    void testListCreation() {
		def user = GDOCUser.findByLoginName("gdocUser");
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
		
		assertEquals 2, UserList.findByName('myNewGeneList').listItems.size()
		assertEquals( "genes egfrQuery", UserList.findByName('myNewGeneList').tags)
    }
}
