

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit UserList</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="list" action="list">UserList List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New UserList</g:link></span>
        </div>
        <div class="body">
            <h1>Edit UserList</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${userListInstance}">
            <div class="errors">
                <g:renderErrors bean="${userListInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${userListInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name">Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:userListInstance,field:'name','errors')}">
                                    <input type="text" id="name" name="name" value="${fieldValue(bean:userListInstance,field:'name')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="author">Author:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:userListInstance,field:'author','errors')}">
                                    <g:select optionKey="id" from="${GDOCUser.list()}" name="author.id" value="${userListInstance?.author?.id}" ></g:select>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="list_comments">Listcomments:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:userListInstance,field:'list_comments','errors')}">
                                    
<ul>
<g:each var="l" in="${userListInstance?.list_comments?}">
    <li><g:link controller="comment" action="show" id="${l.id}">${l?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="comment" params="['userList.id':userListInstance?.id]" action="create">Add Comment</g:link>

                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="list_connections">Listconnections:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:userListInstance,field:'list_connections','errors')}">
                                    
<ul>
<g:each var="l" in="${userListInstance?.list_connections?}">
    <li><g:link controller="userListConnection" action="show" id="${l.id}">${l?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="userListConnection" params="['userList.id':userListInstance?.id]" action="create">Add UserListConnection</g:link>

                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="list_items">Listitems:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:userListInstance,field:'list_items','errors')}">
                                    
<ul>
<g:each var="l" in="${userListInstance?.list_items?}">
    <li><g:link controller="userListItem" action="show" id="${l.id}">${l?.encodeAsHTML()}</g:link></li>
</g:each>
</ul>
<g:link controller="userListItem" params="['userList.id':userListInstance?.id]" action="create">Add UserListItem</g:link>

                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
