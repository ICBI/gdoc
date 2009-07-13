<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Upload List</title>         
    </head>
    <body>
        <div class="body">
					<p class="pageHeading">
            Upload List
					</p>
					<br/>
					<div class="clinicalSearch">
						<br/>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:form name="uploadListForm" action="saveList" method="post" enctype="multipart/form-data">
							List Type: <br/>
							<g:select name="listType" from="${['Patient','gene']}" /><br /><br />
							List Name: <br/>
							<g:textField name="listName" /><br /><br/>
							File: <br/>
							<input type="file" name="file"/>
							<br /><br />
							<input type="submit" value="Submit" />
						</g:form>
					</div>
        </div>
    </body>
</html>
