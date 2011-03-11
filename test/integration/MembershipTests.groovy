class MembershipTests extends GroovyTestCase {

    void testMembership() {
		def user = GDOCUser.get(10)
		def memberships = user.memberships
		memberships.each{ membership ->
			println "MY USER: " + membership.user.username
			println "Collaboration GROUP: " + membership.collaborationGroup.name
			if(membership.collaborationGroup.users){
				println "GROUP: " + membership.collaborationGroup.name + "has the following users: "
				membership.collaborationGroup.users.each{ u ->
					println u.firstName + " " + u.lastName
				}
			}
			println "ROLE:" + membership.role.name
			println "<----------------------------->"
		}
    }
}
