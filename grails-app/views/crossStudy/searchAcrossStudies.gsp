<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="report" />
		</script>
        <title>Cross-Study Results</title>         
    </head>
    <body>
		<jq:plugin name="tooltip"/>
		<jq:plugin name="ui"/>
		<p style="font-size:14pt">Clinical Search Results</p>
		<div id="centerContent">
			<br/>

					<div style="margin:5px 5px 5px 50px">
						<span style="vertical-align:5px"> <label for="list_name">List Name:</label>
							<g:textField name="list_name" size="15"/>
						</span>
					<span class="bla" id="listAdd">Save Selected
						</span><br />
					<span id="message" class="message" style="display:none"></span>
					</div>
					
			<table border="1" style="font-size:.8em">
			<tr>
			<g:if test="${session.myAtts}">
				<th style="padding:7px;font-size:.8em;background-color:silver">GDOC_ID</th>
				<g:each in="${session.myAtts}" var="att">
				<th style="padding:7px;font-size:.8em;background-color:silver">${att}</th>
				</g:each>
				
				<g:set var="found" value="false" />
				<g:set var="myVal" value="N/A" />
				<g:each in="${results.entrySet()}" var="resultV">
				<tr>
					<td>${resultV.key}</td>
				<g:each in="${session.myAtts}" var="ratt">
				
				<g:each in="${resultV.value}" var="resultK">
					<g:each in="${resultK}" var="r">
						<g:if test="${r.key==ratt}">
						<g:set var="found" value="true" />
						<g:set var="myVal" value="${r.value}" />
						</g:if>
					</g:each>
				</g:each>
			
				<g:if test="${found=='true'}">
					<g:set var="found" value="false" />
				</g:if>
				<td style="padding:5px">${myVal}</td>
				<g:set var="myVal" value="N/A" />
				
				</g:each>
				</tr>
				</g:each>
				
			</g:if>
			</tr>
			</table>
			
		</div>
	</body>
	
</hmtl>