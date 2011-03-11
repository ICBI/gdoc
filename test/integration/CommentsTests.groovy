class CommentsTests extends BaseIntegrationTest {

    void testComment() {
		def appUser = GDOCUser.findByUsername('acs224')
		def appCommentUser = GDOCUser.findByUsername('kmr75')
		def intGeneList = new UserList(name:'studyGeneList',author:appUser)
		saveObject(intGeneList)
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
    }
}
