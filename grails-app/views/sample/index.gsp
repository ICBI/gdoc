<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
		</script>
        <title>Search Samples</title>         
    </head>
    <body>
			<p style="font-size:14pt">Search Sample Data</p>
	
	<div id="centerContent" class="welcome">
			<br/>
			<g:form name="searchForm" action="search">
					<div class="clinicalSearch">
						<br/>
						<div style="float: left">
							Select Shared Resource:	
						</div>
						<br/>
						<br/>
						<g:select name="datasource" 
								noSelection="${['':'All']}"
								from="${session.dataSourceMap['Sample']}" />
						<br/>
						<br/>
					</div>
				<br/>

				<br/>
				<g:submitButton name="submit" value="Search"/>
			</g:form>

	</div>
	</body>
	
</hmtl>