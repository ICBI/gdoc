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
		securityService.share(item, groups)
	}
	flash.message = params.name + "has been shared with: "
	groups.each{
		flash.message+=it + " | "
	}
	redirect(action:share,params:[success:true,groups:groups,name:params.name])
	}
}

def share = {
	
}

}