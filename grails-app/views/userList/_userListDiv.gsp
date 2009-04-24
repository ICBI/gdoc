<g:javascript library="jquery"/></head>
 	<%--<div id="${userListInstance.name}_content" style="border:1px solid black;display:none">
	<table>
		<g:each in="${userListInstance.list_items}" status="j" var="list_item">
			<tr><td>${list_item.value}</td>
				<td><a href="javascript:void(0)" 	onclick="${remoteFunction(action:'deleteListItem',id:list_item.id,update:userListInstance.name+'_content')}return false;">delete</a></td>
			</tr>

		</g:each>
	</table>
	</div>--%>
	<table>
		
		<g:if test="${flash.message}">
		<tr><td><div class="message">${flash.message}</div></td></tr>
		</g:if>
		<g:each in="${list_items}" status="j" var="list_item">
			<tr><td>${list_item.value}</td>
				<td><a href="javascript:void(0)" 	onclick="if(confirm('Are you sure?')){${remoteFunction(action:'deleteListItem',id:list_item.id,update:userListInstance.name+'_content')}return false;}">delete</a></td>
			</tr>

		</g:each>
	</table>
