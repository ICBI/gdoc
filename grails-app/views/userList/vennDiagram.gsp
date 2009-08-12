<html>
    <head>
        <meta name="layout" content="report" />
        <title>Venn Diagram</title>     
		<g:render template="/common/flex_header"/>
		<g:javascript library="jquery"/>
    </head>
    <body>
	<g:javascript>
	
	function getVenn(){
		return '${vennJSON}';
	}
	
	</g:javascript>
	<br/>
	<p style="font-size:14pt">Venn Diagram Results</p>
	
		<br/>	
				<g:flex component="VennDiagram" width="500px" height="300px" />
	
	</body>
	
</hmtl>