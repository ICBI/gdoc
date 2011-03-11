<html>
    <head>
        <title>GDOC - Administration</title>
		<meta name="layout" content="main" />
	
    </head>
    <body>
		
		<div id="centerContent">
			<p style="font-size:14pt">GDOC Admin Panel - User ${user.username} Details</p><br />
			<g:if test="${!user}">
				No results found.
			</g:if>
			<g:else>
				<span style="font-size:12pt">User attributes:</span>
				<table class="studyTable">
					<tr style="background-color:silver">
					<th>ID</th>
					<th>First Name</th>
					<th>Last Name</th>
					<th>User Id (Login)</th>
					<th>Email</th>
					<th>Organization</th>
					</tr>
					<tr>
						<td>${user.id}</td>
						<td>${user.firstName}</td>
						<td>${user.lastName}</td>
						<td>${user.username}</td>
						<td>${user.email}</td>
						<td>${user.organization}</td>
					</tr>
				</table> <br />
				
				<span style="font-size:12pt">Memberships:
						<g:if test="${user.memberships}">
							${user.memberships.size()}
						</g:if>
						<g:else>0</g:else>
				</span><br />
				<g:if test="${user.memberships}">
				<table class="studyTable">
					<tr style="background-color:silver">
					<th>Collab Group</th>
					<th>Role</th>
					</tr>
						<g:each in="${user.memberships}" var="mem">
						<tr>
							<td>${mem.collaborationGroup.name}</td>
							<td>${mem.role.name}</td>
						</tr>
						</g:each>
					
				</table>
				</g:if>
				<br />
				<span style="font-size:12pt">Application attributes:</span><br />
				<table class="studyTable">
					<tr style="background-color:silver">
					<th>Lists</th>
					<th>Analyses</th>
					<th>Invitations (Requestor)</th>
					<th>Invitations (Requestee)</th>
					</tr>
					<tr>
						<td>
						<g:if test="${listCount}">
							${listCount}
						</g:if></th>
						<td><g:if test="${analysisCount}">
							${analysisCount}
						</g:if></td>
						<td><g:if test="${user.requestorInvites}">
							${user.requestorInvites.size()}
						</g:if></td>
						<td><g:if test="${user.invitations}">
							${user.invitations.size()}
						</g:if></td>
					</tr>
				</table>
				
				<br />
				<g:if test="${isGdocAdmin}">
					This user is a G-DOC Administrator
				</g:if>
				<g:else>
				<g:form action="deleteUser" method="post">
					<g:hiddenField name="user" value="${user.id}" />
					<g:submitButton name="deleteUser" class="actionButton" style="width:150px" onclick="return confirm('Are you sure?');" value="Delete this user" />
				</g:form>
				</g:else>
			</g:else>
			
		</div>
		
	</body>
	
</html>