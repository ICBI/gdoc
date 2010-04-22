<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
		</script>
        <title>Search Samples</title>         
    </head>
    <body>
			<p style="font-size:14pt">Search Biospecimens</p>
	
	<div id="centerContent" class="welcome">
			<br/>
			<g:if test="${session.options}">
			<g:form name="searchForm" action="search">

					<div class="clinicalSearch">
						<br/>
						<div style="float: left">
							Select Datasource:	
						</div>
						<br/>
						<br/>
						<g:select name="datasource" 
								noSelection="${['':'All']}"
								from="${session.datasources}" />
						<br/>
						<br/>
						<g:each in="${session.options}" var="item">
							<div style="float: left">
								Select ${item.key.decamelize()}:	
							</div>
							<br/>
							<br/>
							<g:select name="${item.key}" 
									noSelection="${['':'All']}"
									from="${item.value}" />
							<br/>
							<br/>
						</g:each>
					</div>

				<br/>

				<br/>
				<g:submitButton name="submit" value="Search"/>
			</g:form>
			</g:if>
			<g:else>
				Error communicating with sample service.  Please try again.
			</g:else>
	</div>
	</body>
	
</hmtl>