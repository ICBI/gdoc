

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main" />
	<title>User Lists</title>
	<g:javascript library="jquery"/>
	<script type="text/javascript">
	function toggle(element){
		$('#'+element+'_content').slideToggle();
		$('.'+element+'_toggle').toggle();
	}
	</script>
</head>
<body>
	<div class="nav">
		<%--<span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
		<span class="menuButton"><g:link class="create" action="create">New UserList</g:link></span>--%>
	</div>
	<div class="body">
		<p class="pageHeading">Manage Lists</p><br />

		<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
	</g:if>



	<g:if test="${userListInstanceList.size()>0}">
	<div class="list" id="test">
		<g:panel title="My Lists" styleClass="welcome" >
		<table class="listTable" width="100%" cellpadding="5">
			<tr>
				<td style="background-color:white;">
					<g:each in="${userListInstanceList}" status="i" var="userListInstance">
					<div id="${userListInstance.name}_div" style="collapse:true;border:0px solid red;margin-bottom:5px;padding:3px 3px 3px 3px">

						<div id="${userListInstance.name}_title" style="border:0px solid black;height: 20px; cursor: pointer;"
						onclick="toggle('${userListInstance.name}');">
						<div style="border:0px solid black;width:30%;float:left">	
							<b>${fieldValue(bean:userListInstance, field:'name')}</b>
						</div>
						<div style="border:0px solid black;float:left; vertical-align: top">
							<img class="${userListInstance.name}_toggle"src="${createLinkTo(dir: 'images', file: 'expand.gif')}"
							width="13"
							height="14" border="0" alt="Show/Hide" title="Show/Hide" />
							<img class="${userListInstance.name}_toggle" src="${createLinkTo(dir: 'images', file: 'collapse.gif')}" 
							width="13"
							height="14" border="0" alt="Show/Hide" title="Show/Hide" style="display:none"/>
						</div>
						</div>
						<div style="border:0px solid black;width:20%;float:right">	
							<a href="javascript:void(0)" style="padding-right:11px">
							<img alt="edit list" src="${createLinkTo(dir: 'images', file: 'pencil.png')}" /></a>

							<a href="javascript:void(0)" onclick="if(confirm('Are you sure?')){${remoteFunction(action:'deleteList',
							id:userListInstance.id,update:userListInstance.name+'_div')}return false;}">
							<img alt="delete list" src="${createLinkTo(dir: 'images', file: 'cross.png')}"/></a>
						</div>

					
					<div id="${userListInstance.name}_content" style="border:0px solid black;display:none;padding-bottom:5px">
						<g:render template="/userList/userListDiv" model="${['userListInstance':userListInstance,'list_items':userListInstance.list_items]}"/>
					</div>
					<div style="border-bottom:1px solid grey;background-color:#f3f3f3;padding-bottom:5px"><g:if test="${userListInstance.tags.size()>0}">Tags:
						${userListInstance.tags}
					</div>
				</g:if>
			</div>
		</g:each>
	</td>
</tr>
</table>
</g:panel>


</div>

</g:if>
<g:else>
<p>Currently, you have no saved lists.</p>
</g:else>


<div class="paginateButtons">
	<g:paginate total="${UserList.count()}" />
</div>
</div>
</body>
</html>
