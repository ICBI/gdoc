class CollaborationGroup {
	def jdbcTemplate
	static mapping = {
			table 'CSM_PROTECTION_GROUP'
			version false
			id column:'PROTECTION_GROUP_ID'
			name column:'PROTECTION_GROUP_NAME'
	}
	
	String name
	static transients = ['users']
	static hasMany = [invitations:Invitation]
	
	public Object getUsers() {
		if(this.@id) {
			def pgId = this.@id
			def users = []
			def userIds = []
			users = jdbcTemplate.queryForList("select USER_ID from CSM.CSM_USER_GROUP_ROLE_PG where PROTECTION_GROUP_ID = '$pgId'")
			users.each{
				userIds << it.get("USER_ID")
			}
			def protGroupUsers = []
			protGroupUsers = GDOCUser.getAll(userIds)
			return protGroupUsers
		}
	}
	
}