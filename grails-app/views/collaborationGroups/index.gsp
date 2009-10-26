<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />

	<g:javascript library="jquery"/>
	<jq:plugin name="ui"/>
        <title>Collaboration Groups</title>         
    </head>
    <body>
		
		<g:javascript>
		var geneExpression = false;
		$(document).ready(function(){
		  //$("#centerTabs").tabs();
			$('#centerTabs').tabs({ selected: 0 });
		});
		
		function toggleUsers(criteria,showHide) {
			console.log('#'+criteria);
			$('#'+criteria).slideToggle();
				if($('#'+showHide).html() == "Show users"){
						$('#'+showHide).html("Hide users");
				}else if($('#'+showHide).html() == "Hide users"){
							$('#'+showHide).html("Show users");
				}
		}
		</g:javascript>
		
		<p style="font-size:14pt">Collaboration Groups</p>
		<div id="centerContent">
			<br/>
			<div class="tabDiv">
				<div id="centerTabs" class="tabDiv">
				    <ul>
				        <li><a href="#fragment-4"><span>My Collaboration Groups</span></a></li>
				        <li><a href="#fragment-5"><span>Create Group</span></a></li>
				    </ul>
					
					 <div id="fragment-4">
							Managed Groups:
							<g:each in="${managedGroups}" var="mgroup">
								<div>
								<p>${mgroup}</p>
<a href="#" id="${mgroup}_showHide" style="color:#FF6F0F;text-decoration:underline;font-weight:normal"        				onClick="toggleUsers('${mgroup}_usersDiv','${mgroup}_showHide');return false;">Show users</a>
									<div id="${mgroup}_usersDiv" style="display:none">
										users go here
									</div>
								</div>
							</g:each>
							
							Member Groups:
					        <g:each in="${memberGroups}" var="cgroup">
								<div>
								<p>${cgroup.protectionGroup.name}</p>
<a href="#" id="${cgroup.protectionGroup.id}_showHide" style="color:#FF6F0F;text-decoration:underline;font-weight:normal"        				onClick="toggleUsers('${cgroup.protectionGroup.id}_usersDiv','${cgroup.protectionGroup.id}_showHide');return false;">Show users</a>
									<div id="${cgroup.protectionGroup.id}_usersDiv" style="display:none">
										<g:if test="${membership.protectionGroup.users}">
											<g:each in="${membership.protectionGroup.users}" var="user">
												<g:checkbox name="${cgroup.protectionGroup.id}_user" />
													${user.firstName}&nbsp; ${user.lastName}
											</g:each>
										</g:if>
										<g:else>
											no users in this group
										<g:else>
									</div>
								</div>
							</g:each>
					</div>
					
					<div id="fragment-5">
						create
					</div>
					
				</div>
			</div>
			
		</div>
		
		</body>
		
</html>