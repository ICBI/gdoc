<html>
    <head>
        <meta name="layout" content="maxSpaceLayout" />

	<g:javascript library="jquery"/>
	<jq:plugin name="ui"/>
        <title>Collaboration Groups</title>         
    </head>
    <body>
		
		<g:javascript>
		var page = "index"
		$(document).ready(function(){
			$('#centerTabs').tabs({ selected: 0 });
			
			if(page == "create") {
				$('#centerTabs').tabs('select', 1);
			}
			if(page == "invite") {
				$('#centerTabs').tabs('select', 2);
			}
			if(page == "all") {
				$('#centerTabs').tabs('select', 3);
			}
		});
		
		function toggleUsers(criteria,showHide) {
			$('#'+criteria).slideToggle();
				if($('#'+showHide).html() == "Show users"){
						$('#'+showHide).html("Hide users");
				}else if($('#'+showHide).html() == "Hide users"){
							$('#'+showHide).html("Show users");
				}
		}
		</g:javascript>
		
		<g:if test="${flash.cmd instanceof CreateCollabCommand}">
			<g:javascript>
				var page = "create"
			</g:javascript>
		</g:if>
		<g:if test="${flash.cmd instanceof InviteCollabCommand}">
			<g:javascript>
				var page = "invite"
			</g:javascript>
		</g:if>
		<g:if test="${params.unauthorizedGroup || params.requestGroupAccess}">
			<g:javascript>
				var page = "all"
			</g:javascript>
		</g:if>
		
		<p style="font-size:14pt">Collaboration Groups</p>
		<div id="centerContent">
			<br/>
			<g:if test="${flash.message}">
				<div class="message" style="font-size:.8em;width:85%">${flash.message}</div>
			</g:if>
			<div class="tabDiv" style="width:85%">
				<div id="centerTabs" class="tabDiv">
				    <ul>
				        <li><a href="#fragment-4"><span>My Collaboration Groups</span></a></li>
				        <li><a href="#fragment-5"><span>Create Group</span></a></li>
						<li><a href="#fragment-6"><span>Invite Users</span></a></li>
						<li><a href="#fragment-7"><span>View Other Groups</span></a></li>
				    </ul>
					
					 <div id="fragment-4">
	<g:render template="/collaborationGroups/collabTable" model="${['managedMemberships':managedMemberships,'otherMemberships':otherMemberships]}"/>
					</div>
				
					<div id="fragment-5">
						<g:render template="/collaborationGroups/createGroupForm" />	
					</div>
					
					<div id="fragment-6">
						<g:render template="/collaborationGroups/inviteUsersForm" />
					</div>
					
					<div id="fragment-7">
						<g:render template="/collaborationGroups/allCollabTable" model="${['allMemberships':allMemberships]}"/>
					</div>
					
				</div>
			</div>
			
		</div>
		
		</body>
		
</html>