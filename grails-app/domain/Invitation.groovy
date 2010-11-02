class Invitation {

    static mapping = {
		table 'INVITATION'
	}
	static belongsTo = [requestor:GDOCUser]
	
	Date dateCreated
	Date lastUpdated
	GDOCUser invitee
	GDOCUser requestor
	InviteStatus status
	CollaborationGroup group
}
