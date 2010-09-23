<g:javascript library="jquery"/>
<g:form action="createCollaborationGroup">
<g:if test="${flash.cmd instanceof CreateCollabCommand}">
	<div class="errorDetail" ><g:renderErrors bean="${flash.cmd?.errors}" field="collaborationGroupName" /></div>
</g:if>
<table class="studyTable" style="font-size:1.05em;width:400px">
	<tr>
		<td>Group name:</td>
		<td><g:textField name="collaborationGroupName" /></td>
	</tr>
	<tr>
		<td>Description:</td>
		<td><g:textArea name="description" /></td>
	</tr>
	<tr>
		<td colspan="2"><g:submitButton name="createCollaborationGroup" class="actionButton" style="float:right" value="Create" /></td>
	</tr>
</table>
</g:form>
