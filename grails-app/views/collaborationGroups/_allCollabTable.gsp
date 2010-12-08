<g:javascript library="jquery"/>
<span style="font-size:.9em;color:red">* This table shows groups for which you currently do not have access.<br />
	To request access, select the option under the group you wish to join.</span>
<table class="studyTable" style="font-size:1.05em;width:450px">
	<tr><th>Group Name</th>
		<!--th>Members</th-->
	</tr>
<g:if test="${allMemberships}">						
<g:each in="${allMemberships}" var="allMembership">

<tr>
<td valign="top">${allMembership.name}<br />
<span style="padding-bottom:8px"><g:link action="requestAccess" controller="collaborationGroups" onclick="return confirm('Are you sure?');" params="[collaborationGroupName:allMembership.name]">request access?</g:link></span>
</td>
<!--td style="width:55%" valign="top">
	<g:if test="${allMembership.users}">
<a href="#" id="${allMembership.id}_showHide" style="color:#FF6F0F;text-decoration:underline;font-weight:normal"        				onClick="toggleUsers('${allMembership.id}_usersDiv','${allMembership.id}_showHide');return false;">Show users</a></g:if>
	<g:else>no users have been added to this group</g:else>
	<div id="${allMembership.id}_usersDiv" style="display:none">
		<g:if test="${allMembership.users}">
		<ul>
			<g:each in="${allMembership.users}" var="user">
				<li id='${user.id}${allMembership.name}' style='padding:3px 3px 3px 3px'>${user.firstName}&nbsp;${user.lastName}</li>
			</g:each>
		</ul>
		</g:if>
		<g:else>
			no users in this group
		</g:else>
	</div>
</td-->
</tr>
</g:each>
</g:if>
</table>
