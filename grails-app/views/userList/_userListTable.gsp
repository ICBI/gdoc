<g:javascript library="jquery"/></head>

		
	
		 <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
           </g:if>
			
	
    <g:each in="${userListInstanceList}" status="i" var="userListInstance">
       	<g:panel id="${i}" title="${fieldValue(bean:userListInstance, field:'name')}" styleClass="welcome" collapse="true">

<table class="studyTable" width="100%">
	<tr>
		<th colspan="2"><span style="border:0px solid black;margin-right:25px">Item</span>
						<span style="border:0px solid black;margin-left:50px">
						<a href="javascript:void(0)" onclick="${remoteFunction(action:'deleteList',id:userListInstance.id,update:'test')}return false;">delete list</a></td>
				                </span></th>
	</tr>
	<g:each in="${userListInstance.list_items}" status="j" var="list_item">
		<tr><td>${list_item.value}</td>
			<td><a href="javascript:void(0)" onclick="${remoteFunction(action:'deleteItem',id:list_item.id,update:'test')}return false;">delete</a></td>
		</tr>
		
	</g:each>
	<tr><td><g:if test="${userListInstance.tags.size()>0}">Tags:
				${userListInstance.tags}
			</g:if>
			</td>
	</tr>
</table>

	</g:panel>

</g:each>

