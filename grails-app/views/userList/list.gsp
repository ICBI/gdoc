

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>User Lists</title>
		<g:javascript library="jquery"/>
    </head>
    <body>
        <div class="nav">
            <%--<span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New UserList</g:link></span>--%>
        </div>
        <div class="body">
            <p class="pageHeading">Manage Lists</p>
			
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
			
		
			<g:if test="${userListInstanceList.size()>0}">
            <div class="list" id="test">
               	 		 
				            
			
						<g:render template="/userList/userListTable" model="${['userListInstanceList':userListInstanceList]}"/>
					
				
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
