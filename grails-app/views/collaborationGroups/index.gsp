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
							<g:if test="${managedMemberships}">
							<span style="font-size:1.2em">Managed Groups</span>
							<table class="studyTable" style="font-size:1.05em;width:400px">
								<tr><th>Group Name</th>
									<th>Members</th>
								</tr>							
							<g:each in="${managedMemberships}" var="manMembership">
								
								<tr>
								<td valign="top">${manMembership.collaborationGroup.name}</td>
								<td style="width:75%">
<a href="#" id="${manMembership.collaborationGroup.id}_showHide" style="color:#FF6F0F;text-decoration:underline;font-weight:normal"        				onClick="toggleUsers('${manMembership.collaborationGroup.id}_usersDiv','${manMembership.collaborationGroup.id}_showHide');return false;">Show users</a></p>
									<div id="${manMembership.collaborationGroup.id}_usersDiv" style="display:none">
										<g:if test="${manMembership.collaborationGroup.users}">
										<ul>
											<g:each in="${manMembership.collaborationGroup.users}" var="user">
													<g:if test="${user.loginName != session.userId}">
														<li style="padding:3px 3px 3px 3px">
															<g:checkBox name="userToDelete" value="${user.loginName}" checked="false" />
															&nbsp;${user.firstName}&nbsp;${user.lastName}
														</li>
													</g:if>
											</g:each>
										</ul>
										</g:if>
										<g:else>
											no users in this group
										</g:else>
									</div>
								</td>
								</tr>
							</g:each>
							</table>
							</g:if>
							<br />
							<g:if test="${otherMemberships}">
							<span style="font-size:1.2em">Member Groups</span>
							<table class="studyTable" style="font-size:1.05em;width:400px">
								<tr><th>Group Name</th>
									<th>Members</th>
								</tr>							
							<g:each in="${otherMemberships}" var="otherMembership">
								
								<tr>
								<td valign="top">${otherMembership.collaborationGroup.name}</td>
								<td style="width:75%">
<a href="#" id="${otherMembership.collaborationGroup.id}_showHide" style="color:#FF6F0F;text-decoration:underline;font-weight:normal"        				onClick="toggleUsers('${otherMembership.collaborationGroup.id}_usersDiv','${otherMembership.collaborationGroup.id}_showHide');return false;">Show users</a></p>
									<div id="${otherMembership.collaborationGroup.id}_usersDiv" style="display:none">
										<g:if test="${otherMembership.collaborationGroup.users}">
										<ul>
											<g:each in="${otherMembership.collaborationGroup.users}" var="user">
												<g:if test="${user.loginName != session.userId}">
													<li style="padding:3px 3px 3px 3px">${user.firstName}&nbsp;${user.lastName}</li>
												</g:if>
											</g:each>
										</ul>
										</g:if>
										<g:else>
											no users in this group
										</g:else>
									</div>
								</td>
								</tr>
							</g:each>
							</table>
							</g:if>
							
					</div>
					
					<div id="fragment-5">
						create
					</div>
					
				</div>
			</div>
			
		</div>
		
		</body>
		
</html>