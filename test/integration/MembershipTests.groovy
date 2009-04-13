class MembershipTests extends GroovyTestCase {

    void testMembership() {
		def gd = new GDOCGroup(name:'fcrGroup',study:'FCR');
		gd.save();
		def groups =GDOCGroup.findAll();	
		groups.each{
			println it.name
		}
		def user = new GDOCUser(username:'Kevin');
		user.save();
		def mm = new Membership(user:user, gdocgroup:gd, role:'owner');
		user.addToMemberships(mm);
		user.save();
		assertTrue Membership.findAll().size() == 1;
		user.memberships.each{
			println it.gdocgroup.name;
			println it.role;
		}
		
    }
}
