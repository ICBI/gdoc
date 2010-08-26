<html>
    <head>
        <title>GDOC - Administration</title>
		<meta name="layout" content="main" />
		<g:javascript library="jquery"/>
		
    </head>
    <body>
	
		<div id="centerContent">
			<p style="font-size:14pt">GDOC Admin Panel</p><br />
			<table style="border:1px solid black;width:85%">
				<tr>
					<th style="background-color:silver">Data-Loading Tasks</th>
				</tr>
				<tr>
					<td>
						<div>
						<div style="padding:7px;background-color:seashell;">
						* <a href="#">reload data availability</a> (disabled)
						<span id="status" style="margin-left:75%">Status</span>
						<span id="spinner" style="float:right">spinner</span>
						</div>
						
						<div style="padding:5px;" id="loaded">
						Currently loaded studies:<g:if test="${loadedStudies}">${loadedStudies.size()}</g:if></div>
								<div style="padding:5px;background-color:#f2f2f2;">
								<g:if test="${loadedStudies}">
								${loadedStudies}
								</g:if>
								<g:else>
								no studies loaded
								</g:else>
								</div>
						</div>
					</td>
				</tr>
			</table>
			
			
		</div>
		
	</body>
	
</html>