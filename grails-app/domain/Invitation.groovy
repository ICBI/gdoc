class Invitation {

    static mapping = {
		table 'INVITATION'
	}
	static belongsTo = [requestor:GDOCUser, group:CollaborationGroup]
	
	Date dateCreated
	Date lastUpdated
	GDOCUser invitee
	GDOCUser requestor
	InviteStatus status
	CollaborationGroup group
}
