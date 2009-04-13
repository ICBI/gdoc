class UserListItemTests extends GroovyTestCase {

    void testUserListItem() {
			def user = new GDOCUser(username:"gdocUser");
			user.save();
			def list = new UserList(name:'myNewPatientList',author:user);
			def list_items = []
			list.save();
			def savedList = UserList.findByName('myNewPatientList');
			savedList.addToList_items(new UserListItem(value:'12345'))
			savedList.addToList_items(new UserListItem(value:'1235GH'))
			println UserListItem.findByValue("12345").value
			assertEquals 2, UserListItem.findAllByList(list).size()

    }
}
