<g:javascript library="jquery"/>
<g:form action="createCollaborationGroup">
<g:if test="${flash.cmd instanceof CreateCollabCommand}">
	<g:renderErrors bean="${flash.cmd?.errors}" field="collaborationGroupName" />
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
		<td colspan="2"><g:submitButton name="createCollaborationGroup" value="Create" /></td>
	</tr>
</table>
</g:form>
