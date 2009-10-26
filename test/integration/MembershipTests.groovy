class MembershipTests extends GroovyTestCase {

    void testMembership() {
		def user = GDOCUser.get(10)
		def memberships = user.groups
		memberships.each{ membership ->
			println "MY USER: " + membership.user.loginName
			println "GROUP: " + membership.protectionGroup.name
			if(membership.protectionGroup.users){
				println "GROUP: " + membership.protectionGroup.name + "has the following users: "
				membership.protectionGroup.users.each{ u ->
					println u.firstName + " " + u.lastName
				}
			}
			println "ROLE:" + membership.role.name
			println "<----------------------------->"
		}
    }
}
