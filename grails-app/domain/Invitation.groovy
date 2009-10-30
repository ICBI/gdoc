class Invitation {

    static mapping = {
		table 'INVITATION'
	}
	
	Date dateCreated
	Date lastUpdated
	GDOCUser invitee
	GDOCUser requestor
	InviteStatus status
	CollaborationGroup group
}
