<html>
    <head>
        <title>Cross-Study Data</title>
		<meta name="layout" content="main" />
    </head>
    <body>
		<jq:plugin name="ui"/>
		<jq:plugin name="tooltip"/>
        <h1 style="font-size:14pt;margin-top:10px;margin-bottom:10px">Cross-Study Clinical Attributes</h1>
		<div id="centerContent">
        <div style="padding-left:50px">
			<g:if test="${myAtts}">
				<g:form action="searchAcrossStudies">
				<div class="clinicalSearch">
					<div style="float: left;">
						Choose Studies
						<img alt="" class="info" src="/gdoc/images/information.png" border="0">
					</div>
					<div style="float: right; vertical-align: middle;">
						<img class="close" src="/gdoc/images/cross.png" border="0">
					</div>
					<br>
					<br>
					<div align="left">
						<select id="clinical_RECURRENCE_ANY" size="5" multiple="true">
							<option value="">CLARKE-LIU</option>
							<option value="NO">LOI</option>
							<option value="YES">CRC_PILOT</option>
							<option>EARLY-HCC</option>
							<option>FCR</option>
							<option>PRE-OP</option>
						</select>

					</div>
					<br>
				</div>
				<g:each in="${myAtts}" var="j">
					<div class="clinicalSearch">
						<div style="float: left;">
							${j}
							<img alt="" class="info" src="/gdoc/images/information.png" border="0">
						</div>
						<div style="float: right; vertical-align: middle;">
							<img class="close" src="/gdoc/images/cross.png" border="0">
						</div>
						<br>
						<br>
						<div align="left">
							<select id="clinical_RECURRENCE_ANY">
								<option value="">Select One...</option>
								<option value="NO">NO</option>
								<option value="YES">YES</option>
							</select>

						</div>
						<br>
					</div>
				</g:each>
				<g:submitButton class="actionButton" style="float:right" name="search" value="Search" />
				</g:form>
			</g:if>
		</div>
       </div>
        
	

    </body>
</html>