<g:javascript library="jquery"/>
	<table>
		
		<g:if test="${flash.message}">
		<tr><td><div class="message">${flash.message}</div></td></tr>
		</g:if>
		<g:each in="${listItems}" status="j" var="list_item">
			<tr><td>${list_item.value}</td>
				<g:if test="${session.userId.equals(userListInstance.author.loginName)}">
				<td><a href="javascript:void(0)" 	onclick="if(confirm('Are you sure?')){${remoteFunction(action:'deleteListItem',id:list_item.id,update:userListInstance.name+'_content')}return false;}">delete</a></td>
				</g:if>
			</tr>

		</g:each>
	</table>
