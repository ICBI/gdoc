class CommentsTests extends GroovyTestCase {

    void testComment() {
	def appUser = new GDOCUser(username:'studyUser').save();
	def appCommentUser = new GDOCUser(username:'studyCommentUser').save();
	def intGeneList = new UserList(name:'studyGeneList',author:appUser).save();
	intGeneList.addToList_items(new UserListItem(value:'ER2'))
		intGeneList.addToList_items(new UserListItem(value:'bR2'))
			intGeneList.addToList_items(new UserListItem(value:'vfrR'))
	UserList.findByAuthor(appUser).each{
		println it.name
		it.list_items.each{
			println it.value
		}
	}
	def commentText = "Wow, that is an interesting list"
	def comment = new Comments(user:appCommentUser, comment_text:commentText,list:intGeneList)
	intGeneList.addToList_comments(comment);
	appCommentUser.addToComments(comment);
	def recComment = Comments.findByUser(appCommentUser);
	recComment.each{
		println it.comment_text
		println it.user
		println it.list	
	}
	def recList = UserList.find(intGeneList)
	println recList.list_comments
	def usersList = GDOCUser.find(appCommentUser)
	println usersList.comments
    }
}
