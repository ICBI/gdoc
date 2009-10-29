<g:javascript library="jquery"/>
<g:if test="${flash.cmd instanceof DeleteCollabUserCommand}">
	<p><g:renderErrors bean="${flash.cmd?.errors}" field="users" /></p>
</g:if>

<table>
	<tr><td style="padding-right:15px;padding-bottom:15px" valign="top">
		<table class="studyTable" style="font-size:1.05em;width:375px">
			<tr><th colspan="2">Managed Groups</th></tr>
			<tr><th>Group Name</th>
				<th>Members</th>
			</tr>
<g:if test="${managedMemberships}">			
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
</g:if>
<g:else>
<tr><td colspan="2">
You do not manage any groups
</td></tr>
</g:else>
</table>
</td>

<td style="padding-left:10px" valign="top">
<table class="studyTable" style="font-size:1.05em;width:275px">
	<tr><th>Invitations and Messages</th></tr>
	<tr><td>
		<g:render template="/notification/invitationTable" />
	</td></tr>
</table>
</td></tr>

<tr><td colspan="2" style="padding-right:15px">
	<table class="studyTable" style="font-size:1.05em;width:375px">
		<tr><th colspan="2">Member Groups</th></tr>
		<tr><th>Group Name</th>
			<th>Members</th>
		</tr>
<g:if test="${otherMemberships}">						
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
</g:if>
<g:else>
<tr><td colspan="2">
You are not serving as a member in any other groups
</td></tr>
</g:else>
</table>
</td><td>&nbsp;</td></tr>
</table>
