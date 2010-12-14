<html>
<head>
	<meta name="layout" content="splash" />
	<title>Registration Request Confirmation</title>
	
</head>
<body>
	<br /><br />
	<g:if test="${flash.message}">
			<div class="message" style="margin:0 auto;">${flash.message}</div>
	</g:if>
	<g:else>
			<div style="margin:0 auto;" align="center">return to <g:link controller="home">G-DOC home</g:link></div>
	</g:else>
	
</body>

</html>
