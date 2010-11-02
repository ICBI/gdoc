class GDOCUser {
    static mapping = {
		table 'CSM_USER'
		version false
		id column:'user_id'
		loginName column: "login_name", unique: true
		firstName column: "first_name"
		lastName column: "last_name"
		email column: "email_id"
		lastLogin column: "end_date"
	}
	
	String loginName
	String firstName
	String lastName
	String password
	String email
	String department
	String organization
	Date lastLogin
	
	static constraints = {
	        department(nullable: true)
	}
	
	static mappedBy = [invitations:'invitee',requestorInvites:'requestor']	
	static hasMany = [memberships:Membership,comments:Comments, analysis:SavedAnalysis, invitations:Invitation, requestorInvites:Invitation, lists:UserList]
	
    
}