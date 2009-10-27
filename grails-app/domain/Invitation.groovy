class Invitation {

    static mapping = {
		table 'INVITATION'
	}
	
	GDOCUser invitee
	GDOCUser requestor
	InviteStatus status
	CollaborationGroup group
}
