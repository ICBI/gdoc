<g:javascript library="jquery"/>
<g:form action="inviteUsers">
<g:if test="${flash.cmd instanceof InviteCollabCommand}">
	<div class="errorDetail"><g:renderErrors bean="${flash.cmd?.errors}" as="list" /></div>
</g:if>
<table class="studyTable" style="font-size:1.05em;width:400px">
	<tr>
		<td align="top">User(s):</td>
		<td>
			<g:select name="users"
					  from="${GDOCUser.list()}"
					  noSelection="['':'-Choose one or more users-']"
		          	  optionValue="${{it?.lastName?.toString() + ', ' + it?.firstName?.toString()}}"
				      optionKey="loginName" 
					  multiple="true"
					  size="3"/>
		</td>
	</tr>
	<tr>
		<td>Group:</td>
		<td>
			<g:if test="${managedMemberships}">
			<g:select name="collaborationGroupName" from="${managedMemberships}"
		          noSelection="['':'-Choose group-']"
				  optionValue="name"
				  optionKey="name" />
			</g:if>
		</td>
	</tr>
	<tr>
		<td colspan="2"><g:submitButton class="actionButton" style="float:right" name="inviteButton" value="Send Invite" /></td>
	</tr>
</table>
</g:form>
