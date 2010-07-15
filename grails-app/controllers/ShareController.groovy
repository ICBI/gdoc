import SecurityException

class ShareController {
    def securityService
//def index = { redirect(action:share,params:params) }

def shareItem = {
	if(!params.groups) {
						flash.message = "no groups have been selected. Please select groups."
		                redirect(controller:'share',action:'share',params:[id:params.itemId,name:params.name,
																	type:params.type,failure:true])
	}else{
	print params
	def item
	def groups = []
	def alreadySharedGroups = []
 	print groups
	
	if(params.groups instanceof String[]){
		params.groups.each{
			groups << it
		}
	}else if (params.groups instanceof String){
		groups << params.groups
	}
	
	if(params.type.equals(Constants.SAVED_ANALYSIS)){
		print 'share saved analysis: ' + params.itemId
		item = SavedAnalysis.get(params.itemId)
	}
	if(params.type.equals(Constants.USER_LIST)){
		print 'share user list: ' + params.itemId
		item = UserList.get( params.itemId )
	}
	if(item){
		try{
			alreadySharedGroups = securityService.groupsShared(item)
			if(alreadySharedGroups){
			 alreadySharedGroups.each{
				groups.remove(it)
			 }
			log.debug "removed already shared groups: $alreadySharedGroups"
			log.debug "remaing groups: $groups"
			}
			if(!groups.isEmpty()){
			log.debug "sharing with groups: $groups"
			securityService.share(item, groups)
			}
		}catch(SecurityException se){
				se.printStackTrace(System.out);
				flash['message'] = 'Sorry, there has been a problem sharing this item'
				redirect(action:share,params:[success:false,groups:groups,name:params.name])
		}
	}
	if(groups.isEmpty()){
			log.debug "no item is to be shared"
			flash['message'] = 'This item has already been shared with the following: '
			for(int i=0;i<alreadySharedGroups.size();i++){
				if((i+1)==alreadySharedGroups.size()){
					flash.message+=alreadySharedGroups[i]
				}else{
					flash.message+=alreadySharedGroups[i] + " , "
				}
			}
			redirect(action:share,params:[failure:true,name:params.name])
	}else{
		log.debug "shared to groups: $groups"
		flash.message = params.name + " has been shared with: "
		for(int i=0;i<groups.size();i++){
			if((i+1)==groups.size()){
				flash.message+=groups[i]
			}else{
				flash.message+=groups[i] + " , "
			}
		}
		redirect(action:share,params:[success:true,groups:groups,name:params.name])
		}
	}
}

def share = {
	
}

}