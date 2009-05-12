class CommentsTests extends GroovyTestCase {

    void testComment() {
	def appUser = new GDOCUser(username:'studyUser').save();
	def appCommentUser = new GDOCUser(username:'studyCommentUser').save();
	def intGeneList = new UserList(name:'studyGeneList',author:appUser).save();
	intGeneList.addToListItems(new UserListItem(value:'ER2'))
		intGeneList.addToListItems(new UserListItem(value:'bR2'))
			intGeneList.addToListItems(new UserListItem(value:'vfrR'))
	UserList.findByAuthor(appUser).each{
		println it.name
		it.listItems.each{
			println it.value
		}
	}
	def commentText = "Wow, that is an interesting list"
	def comment = new Comments(user:appCommentUser, commentText:commentText,list:intGeneList)
	intGeneList.addToListComments(comment);
	appCommentUser.addToComments(comment);
	def recComment = Comments.findByUser(appCommentUser);
	recComment.each{
		println it.commentText
		println it.user
		println it.list	
	}
	def recList = UserList.find(intGeneList)
	println recList.listComments
	def usersList = GDOCUser.find(appCommentUser)
	println usersList.comments
    }
}
