class UserListControllerTests extends GroovyTestCase {
	
    void testSave() {
	
	def list_controller = new UserListController();
	def user = new GDOCUser(username:"list_gdocUser");
	def user2 = new GDOCUser(username:"list_gdocUser2");
	user.save();
	user2.save();
	
	list_controller.params.name = "test"
	list_controller.params."author.id" = GDOCUser.find(user).id
	list_controller.save();
	def userList = UserList.findByAuthor(user);
	
	assertEquals 1, UserListConnection.findByUser(user).count();
	assertEquals "/userList/show/" + UserList.findByAuthor(user).id, 
	        list_controller.response.redirectedUrl

    }

	void testSaveFromQuery() {
	
	def list_controller2 = new UserListController();
	def my_user = new GDOCUser(username:"my_gdocUser");
	my_user.save();
	
	//list_controller2.params.name = "testFromQuery"
	list_controller2.params."author.username" = GDOCUser.find(my_user).username
	list_controller2.saveFromQuery();
	def userList = UserList.findByAuthor(my_user);
	
	assertEquals 1, UserListConnection.findByUser(my_user).count();


    }

}
