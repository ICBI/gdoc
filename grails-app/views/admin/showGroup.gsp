<html>
    <head>
        <title>GDOC - Administration</title>
		<meta name="layout" content="main" />
	
    </head>
    <body>
		
		<div id="centerContent">
			<p style="font-size:14pt">GDOC Admin Panel - Group ${group.name} Details</p><br />
			<g:if test="${!group}">
				No results found.
			</g:if>
			<g:else>
				<span style="font-size:12pt">Group attributes:</span>
				<table class="studyTable">
					<tr style="background-color:silver">
					<th>ID</th>
					<th>Name</th>
					<th>Description</th>
					<th>Members</th>
					</tr>
					<tr>
						<td>${group.id}</td>
						<td>${group.name}</td>
						<td>${group.description}</td>
						<td>${members}</td>
					</tr>
				</table> <br />
				
				<g:form action="deleteGroup" method="post">
					<g:hiddenField name="group" value="${group.id}" />
					<g:submitButton name="deleteGroup" class="actionButton" style="width:150px" onclick="return confirm('Are you sure?');" value="Delete this group" />
				</g:form>
			</g:else>
			
		</div>
		
	</body>
	
</html>