
class UserListConnectionTests extends GroovyTestCase {

    void testListConnection() {
	
			def author = GDOCUser.findByLoginName('gdocUser');
			def mylist = new UserList(name:'myGeneList', author:author);
			println mylist.author.loginName
			
			def list_items = []
			list_items.add(new UserListItem(value:'EGFR'))
			list_items.add(new UserListItem(value:'VEGF'))
			list_items.add(new UserListItem(value:'BRCA1'))
			list_items.each{
				mylist.addToListItems(it)
			}
		    mylist.save();
			def conn = new UserListConnection(user:author,list:mylist, rating:3).save();
			println conn
			assertEquals 1, UserListConnection.findByUser(author).count();
			println UserListConnection.findByList(mylist).rating
			assertEquals 3, UserList.findByAuthor(author).listItems.size()
		
			
    }
}
