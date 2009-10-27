class MembershipTests extends GroovyTestCase {

    void testMembership() {
		def user = GDOCUser.get(10)
		def memberships = user.groups
		memberships.each{ membership ->
			println "MY USER: " + membership.user.loginName
			println "GDOC GROUP: " + membership.gdocGroup.name
			if(membership.gdocGroup.users){
				println "GROUP: " + membership.gdocGroup.name + "has the following users: "
				membership.gdocGroup.users.each{ u ->
					println u.firstName + " " + u.lastName
				}
			}
			println "ROLE:" + membership.role.name
			println "<----------------------------->"
		}
    }
}
