<g:javascript library="jquery"/></head>
 	<g:if test="${flash.message}">
           <div class="message">${flash.message}</div>
     </g:if>
	<br />
		<g:panel title="My Lists" styleClass="welcome" >
			<table class="listTable" width="100%" cellpadding="5">
			<g:each in="${userListInstanceList}" status="i" var="userListInstance">
						<tr>
							<td style="background-color:white;">
								<table>
									<tr>
										<td>${fieldValue(bean:userListInstance, field:'name')}</td>
								
								
								<td id="${userListInstance.name}_title" style="height: 20px; cursor: pointer;position:relative;float:middle">
									<div style="float: left; vertical-align: middle">
								<img class="${userListInstance.name}_toggle"src="${createLinkTo(dir: 'images', file: 'expand.gif')}" width="13"
						         height="14" border="0" alt="Show/Hide" title="Show/Hide" />
								<img class="${userListInstance.name}_toggle" src="${createLinkTo(dir: 'images', file: 'collapse.gif')}" width="13"
								         height="14" border="0" alt="Show/Hide" title="Show/Hide" style="display:none"/>
									
									</div>
								</td>
								<td>
									<a href="javascript:void(0)" onclick="${remoteFunction(action:'deleteList',
									id:userListInstance.id,update:'test')}return false;">delete list</a>         
						        </td>
									</tr>
								</table>
									
								<div id="${userListInstance.name}_content" style="display:none">
								<table>
									<g:each in="${userListInstance.listItems}" status="j" var="list_item">
										<tr><td>${list_item.value}</td>
											<td><a href="javascript:void(0)" 	onclick="${remoteFunction(action:'deleteItem',id:list_item.id,update:'test')}return false;">delete</a></td>
										</tr>

									</g:each>
								</table>
								</div>
									
							</td>
						</tr>
						<tr><td style="border-bottom:1px solid grey"><g:if test="${userListInstance.tags.size()>0}">Tags:
									${userListInstance.tags}
								</g:if>
								<script type="text/javascript">
									$(document).ready(function() {
										$('#${userListInstance.name}_title').click(function() {
											$('#${userListInstance.name}_content').slideToggle();
											$('.${userListInstance.name}_toggle').toggle();
										});
								  });
								</script>
								</td>
						</tr>
						
								
			</g:each>
			</table>
		</g:panel>	
		
	
	
			
	
    <%--<g:each in="${userListInstanceList}" status="i" var="userListInstance">
       	<g:panel id="${i}" title="${fieldValue(bean:userListInstance, field:'name')}" styleClass="welcome" collapse="true">

		<table class="studyTable" width="100%">
	<tr>
		<th colspan="2"><span style="border:0px solid black;margin-right:25px">Item</span>
						<span style="border:0px solid black;margin-left:50px">
						<a href="javascript:void(0)" onclick="${remoteFunction(action:'deleteList',id:userListInstance.id,update:'test')}return false;">delete list</a></td>
				                </span></th>
	</tr>
	<g:each in="${userListInstance.listItems}" status="j" var="list_item">
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

</g:each>--%>

