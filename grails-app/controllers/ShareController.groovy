import SecurityException

@Mixin(ControllerMixin)
class ShareController {
    def securityService
//def index = { redirect(action:share,params:params) }

def shareItem = {
	if(!params.groups || !params.type) {
						flash.message = "no groups or no type has been selected. Please select groups."
		                redirect(controller:'share',action:'share',params:[id:params.itemId,name:params.name,
																	type:params.type,failure:true])
	}else{
	log.debug params
	def item
	def groups = []
	def alreadySharedGroups = []
 	log.debug groups
	
	if(params.groups instanceof String[]){
		params.groups.each{
			groups << it 
		}
	}else if (params.groups instanceof String){
		groups << params.groups
	}
	
	if(params.type.equals(Constants.SAVED_ANALYSIS)){
		if(isAnalysisAuthor(params.itemId)){
			log.debug 'share saved analysis: ' + params.itemId
			item = SavedAnalysis.get(params.itemId)
		}
		else{
			log.debug "user is NOT permitted to share analysis"
			redirect(controller:'policies',action:'deniedAccess')
			return
		}
	}
	if(params.type.equals(Constants.USER_LIST)){
		if(isListAuthor(params.itemId)){
			log.debug 'share user list: ' + params.itemId
			item = UserList.get( params.itemId )
		}
		else{
			log.debug "user is NOT permitted to share list"
			redirect(controller:'policies',action:'deniedAccess')
			return
		}
	}
	if(item && params.type){
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
				log.error("error with sharing item", se)
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
	def item
	if(params.id && params.type){
		item = getItem(params.id, params.type)
		if(item) {
			def alreadySharedGroups = []
			alreadySharedGroups = securityService.groupsShared(item)
			if(alreadySharedGroups){
				flash.message = alreadySharedGroups
				[groups:alreadySharedGroups]
			}
		}
	}
}

def getItem(id,type){
	def item
	if(type.equals(Constants.SAVED_ANALYSIS)){
		if(isAnalysisAuthor(id)){
			log.debug 'attempting to share saved analysis: ' + id
			item = SavedAnalysis.get(id)
			return item
		}
		else{
			log.debug "user is NOT permitted to share analysis"
			redirect(controller:'policies',action:'deniedAccess')
			return
		}
	}
	if(type.equals(Constants.USER_LIST)){
		if(isListAuthor(id)){
			log.debug 'attempting to share user list: ' + id
			item = UserList.get(id)
			return item
		}
		else{
			log.debug "user is NOT permitted to share list"
			redirect(controller:'policies',action:'deniedAccess')
			return
		}
	}
	return item
}

}