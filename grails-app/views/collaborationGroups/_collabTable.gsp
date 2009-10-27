<g:javascript library="jquery"/>

<g:if test="${managedMemberships}">
<g:if test="${flash.cmd instanceof DeleteCollabUserCommand}">
	<p><g:renderErrors bean="${flash.cmd?.errors}" field="users" /></p>
</g:if>
<span style="font-size:1.2em">Managed Groups</span>
<table class="studyTable" style="font-size:1.05em;width:400px">
	<tr><th>Group Name</th>
		<th>Members</th>
	</tr>							
<g:each in="${managedMemberships}" var="manMembership">
	
	<tr>
	<td valign="top">${manMembership.collaborationGroup.name}</td>
	<td style="width:75%">
		<g:if test="${manMembership.collaborationGroup.users}">
<a href="#" id="${manMembership.collaborationGroup.id}_showHide" style="color:#FF6F0F;text-decoration:underline;font-weight:normal"        				onClick="toggleUsers('${manMembership.collaborationGroup.id}_usersDiv','${manMembership.collaborationGroup.id}_showHide');return false;">Show users</a></g:if>
		<g:else>no users have been added to this group</g:else>
		<div id="${manMembership.collaborationGroup.id}_usersDiv" style="display:none">
			<g:if test="${manMembership.collaborationGroup.users}">
			<g:form name="${manMembership.id}_removeUserForm" action="deleteUsersFromGroup">
			<g:hiddenField name="collaborationGroupName" value="${manMembership.collaborationGroup.name}" />
			<ul>
				<g:each in="${manMembership.collaborationGroup.users}" var="user">
						<g:if test="${user.loginName != session.userId}">
							<li style="padding:3px 3px 3px 3px">
								<g:checkBox name="users" value="${user.loginName}" checked="false" />
								&nbsp;${user.firstName}&nbsp;${user.lastName}
							</li>
						</g:if>
				</g:each>
			</ul>
			<g:submitButton name="deleteUser" value="Remove Users" />
			</g:form>
			</g:if>			
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
		<g:if test="${otherMembership.collaborationGroup.users}">
<a href="#" id="${otherMembership.collaborationGroup.id}_showHide" style="color:#FF6F0F;text-decoration:underline;font-weight:normal"        				onClick="toggleUsers('${otherMembership.collaborationGroup.id}_usersDiv','${otherMembership.collaborationGroup.id}_showHide');return false;">Show users</a></g:if>
		<g:else>no users have been added to this group</g:else>
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
