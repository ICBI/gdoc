<html>
<head>
	<meta name="layout" content="main" />
	<g:javascript library="jquery" />
	<title>G-DOC Group Comparison Analysis</title>         
</head>
<body>
	
<p style="font-size:14pt">Perform Group Comparison Analysis / <g:link controller="km">KM Plot Analyses</g:link><span style="font-size:.7em"> (clinical and genomic)</span></p>

<div id="studyPicker">
	<g:render template="/studyDataSource/studyPicker"/>
</div>

<div id="searchDiv">
	<g:render template="/analysis/studyForm"/>
</div>

</body>

</hmtl>