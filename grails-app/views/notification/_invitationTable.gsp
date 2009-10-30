<g:javascript library="jquery"/>

<g:set var="availableInvites" value="false" />
<g:if test="${session.invitations}">
<g:if test="${session.invitations['inv'] || session.invitations['req'] || session.invitations['reqAndMan'] || session.invitations['invNotMan']}">
	<g:if test="${session.invitations['inv']}">
		<g:each in="${session.invitations['inv']}" var="inInvite">
		<g:if test="${inInvite.status == InviteStatus.PENDING}">
		<g:set var="availableInvites" value="true" />
		<div class="inviteDiv">${inInvite.requestor.firstName} ${inInvite.requestor.lastName} has requested access to ${inInvite.group.name}
			&nbsp;&nbsp;&nbsp; 
			<g:link action="grantAccess" controller="collaborationGroups" id="${inInvite.id}" params="[user:inInvite.requestor.loginName,group:inInvite.group.name]">grant access</g:link>
			&nbsp;&nbsp;&nbsp;
			<g:link action="rejectInvite" controller="collaborationGroups" id="${inInvite.id}" params="[user:inInvite.requestor.loginName,group:inInvite.group.name]">reject</g:link>
			</div>
		</g:if>
		</g:each>
   </g:if>
	<g:if test="${session.invitations['invNotMan']}">
		<g:each in="${session.invitations['invNotMan']}" var="inInvite">
		<g:if test="${inInvite.status == InviteStatus.PENDING}">
		<g:set var="availableInvites" value="true" />
		<div class="inviteDiv">${inInvite.requestor.firstName} ${inInvite.requestor.lastName} has invited you to join ${inInvite.group.name}
			&nbsp;&nbsp;&nbsp; <br />
			received:<g:formatDate format="EEE MMM d, yyyy" date="${inInvite.dateCreated}"/><br />
			<g:link action="addUser" controller="collaborationGroups" id="${inInvite.id}" params="[user:session.userId,group:inInvite.group.name]">accept invitation</g:link>
			&nbsp;&nbsp;&nbsp;
			<g:link action="rejectInvite" controller="collaborationGroups" id="${inInvite.id}" params="[user:session.userId,group:inInvite.group.name]">reject</g:link>
			</div>
		</g:if>
		</g:each>
	</g:if>
   	<g:if test="${session.invitations['req']}">
		<g:set var="availableInvites" value="true" />
		<g:each in="${session.invitations['req']}" var="reqInvite">
		<div class="inviteDiv">your request for access to ${reqInvite.group.name} is: 
			${reqInvite.status}<br />
			sent:<g:formatDate format="EEE MMM d, yyyy" date="${reqInvite.dateCreated}"/> | updated:<g:formatDate format="EEE MMM d, yyyy" date="${reqInvite.lastUpdated}"/>
		</div>
		</g:each>
  	</g:if>
  	<g:if test="${session.invitations['reqAndMan']}">
		<g:set var="availableInvites" value="true" />
		<g:each in="${session.invitations['reqAndMan']}" var="reqInvite">
		<div class="inviteDiv">your invitation for ${reqInvite.invitee.loginName} to join ${reqInvite.group.name} is: ${reqInvite.status}<br />
		sent:<g:formatDate format="EEE MMM d, yyyy" date="${reqInvite.dateCreated}"/> | updated:<g:formatDate format="EEE MMM d, yyyy" date="${reqInvite.lastUpdated}"/>
		</div>
		</g:each>
	</g:if>
</g:if>
</g:if>
<g:if test="${availableInvites=='false'}">
No pending invitations or messages at this time
</g:if>