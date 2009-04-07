<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
		</script>
        <title>Search Gene Expression Data</title>         
    </head>
    <body>
	<p style="font-size:14pt">Search Gene Expression Data</p>
	<div id="centerContent">
		<br/>
		<g:form name="searchForm" action="search">
				<div class="clinicalSearch">
					<div style="float: left">
						Gene Name	
					</div>
				<br/>
				<br/>
						<g:textField name="geneName"  />
				</div>
			<br/>
			<br/>
			<g:submitButton name="submit" value="Search"/>
		</g:form>
	</div>
	</body>
	
</hmtl>