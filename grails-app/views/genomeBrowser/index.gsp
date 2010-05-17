<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main" />
	<g:javascript library="jquery" />
	<title>GDOC Genome Browser</title>         
</head>
<body>
	
<p style="font-size:14pt">Genome Browser</p>

<div id="studyPicker">
	<g:render template="/studyDataSource/studyPicker"/>
</div>

<div id="searchDiv">
	<g:form name="browseForm" action="view">
		<g:submitButton name="submit" value="Submit"/>
	</g:form>
</div>

</body>

</hmtl>