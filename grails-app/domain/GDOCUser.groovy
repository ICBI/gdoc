class GDOCUser {
    static mapping = {
		table 'CSM_USER'
		version false
		id column:'user_id'
		loginName column: "login_name", unique: true
		firstName column: "first_name"
		lastName column: "last_name"
	}
	
	String loginName
	String firstName
	String lastName
	String password
	
	static mappedBy = [invitations:'invitee']	
	static hasMany = [memberships:Membership,comments:Comments, analysis:SavedAnalysis, invitations:Invitation]
	
	def lists(){
		return UserList.findAllByAuthor(this)
	}
    
}