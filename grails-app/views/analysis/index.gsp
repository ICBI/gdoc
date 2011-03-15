<html>
<head>
	<meta name="layout" content="main" />
	<g:javascript library="jquery" />
	<title>G-DOC Group Comparison Analysis</title>         
</head>
<body>
	
<p style="font-size:14pt">Perform Group Comparison Analysis </p>

<div id="studyPicker">
	<g:render template="/studyDataSource/studyPicker" plugin="gcore"/>
</div>

<div id="searchDiv">
	<g:render template="/analysis/studyForm"/>
</div>

</body>

</hmtl>