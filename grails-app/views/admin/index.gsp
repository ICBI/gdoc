<html>
    <head>
        <title>GDOC - Administration</title>
		<meta name="layout" content="main" />
		<g:javascript library="jquery"/>
		<g:javascript>
			function success(){
					$("#spinner").css("visibility","hidden");
			}
			function loading(){
					$("#spinner").css("visibility","visible");
					$("#status").html("Status: loading")
			}
		</g:javascript>
    </head>
    <body>
	
		<div id="centerContent">
			<p style="font-size:14pt">GDOC Admin Panel</p><br />
			
			<g:if test="${flash.message}">
				<span class="message">${flash.message}</span>
			</g:if>
			
			<table style="border:1px solid black;width:85%">
				<tr>
					<th style="background-color:silver">Data-Loading Tasks</th>
				</tr>
				<tr>
					<td>
						<div>
						<div style="padding:7px;background-color:seashell;">
				<g:link action="reload" onclick="loading()">reload data availability</g:link><br />
						<span id="status">Status: 
							<g:if test="${loadedStudies}">${loadedStudies.size()} studies loaded</g:if>
							<g:else> there are no studies loaded at this time</g:else>
						</span>
						<span id="spinner" style="visibility:hidden;display:inline-table"><img src='/gdoc/images/spinner.gif' alt='Wait'/></span>
						</div>
						
								<div style="padding:5px;background-color:#f2f2f2;">
								<g:if test="${loadedStudies}">
								${loadedStudies}
								</g:if>
								<g:else>
								no studies loaded
								</g:else>
								</div>
						</div>
					</td>
				</tr>
				</table>
				<br />
				<table style="border:1px solid black;width:85%">
				<tr>
					<th style="background-color:silver">User Admin</th>
				</tr>
				<tr>
					<td>
						<div>
						<div style="padding:7px;background-color:seashell;">Find a user<br />
						
						</div>
						
								<div style="padding:5px;background-color:#f2f2f2;">
							     <g:form action="searchUsers">
									User Id (email OR netId): <g:textField name="userId" id="userIdField" /><br /><br />
									<g:submitButton name="search" value="Search Users" />
								</g:form>
								</div>
						</div>
					</td>
				</tr>
			</table>
			
			
		</div>
		
	</body>
	
</html>