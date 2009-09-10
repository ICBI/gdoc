<g:javascript library="jquery"/>
	<table id="${userListInstance.id}_listItems">
		
		<g:each in="${listItems}" status="j" var="list_item">
			<tr>
				<td>
					<g:if test="${userListInstance.tags.contains('gene')}">
						<a href="http://www.genecards.org/cgi-bin/carddisp.pl?gene=${list_item.value}" target="_blank">${list_item.value}</a>
					</g:if>
					<g:else>
						${list_item.value}
					</g:else>
				</td>
				<g:if test="${session.userId.equals(userListInstance.author.loginName)}">
				<td><a href="javascript:void(0)" 	onclick="if(confirm('Are you sure?')){var classn ='${userListInstance.id}_toggle';${remoteFunction(action:'deleteListItem',id:list_item.id,update:userListInstance.id+'_content',onLoading:'showPageSpinner(true,classn)',onComplete:'showPageSpinner(false,classn)')}return false;}">delete</a></td>
				</g:if>
			</tr>

		</g:each>
	</table>
