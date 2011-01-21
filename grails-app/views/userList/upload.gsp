<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <g:javascript library="jquery"/>
		<script type="text/javascript" src="${createLinkTo(dir: 'js', file: 'thickbox.js')}"></script>
        <title>Upload List</title>  
       <link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styles.css')}"/>
		<script type="text/javascript">
			function refreshListPage(){
				window.parent.location.replace(window.parent.location.pathname);
			}
		</script>
    </head>
    <body>
		
		<g:if test="${params.success}">
			<div class="successMessage" style="color:#007000">${flash.message}</div><br />
			<script>refreshListPage();</script>
		</g:if>
        <div class="body">
					<p class="pageHeading">
            Upload List 
					</p>
					<p>(*requires .txt format & will be read as line-delimited)
					</p>
					<div class="clinicalSearch">
						
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
			<g:if test="${flash.error}">
            <div class="errorDetail">${flash.error}</div>
            </g:if>
            <g:form name="uploadListForm" action="saveList" method="post" enctype="multipart/form-data">
							List Type: <br/>
							<g:select name="listType" from="${['patient','gene','reporter']}" /><br /><br />
							List Name: <br/>
							<g:textField name="listName" size="15" maxlength="15"/><br /><br/>
							File: <br/>
							<input type="file" name="file"/>
							<br /><br />
							<input type="submit" value="Submit" />
						</g:form>
					</div>
        </div>
    </body>
</html>
