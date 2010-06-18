<html>
    <head>
        <title>Georgetown Database of Cancer</title>
		<meta name="layout" content="splash" />
		<g:javascript library="jquery"/>
		<g:javascript src="jquery/scrollTable/scrolltable.js"/>
		<g:javascript src="jquery/scrollTable/jscrolltable.js"/>
	
		<!-- styling -->
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'scrollable-navig.css')}"/>
		
		
    </head>
    <body>
				<jq:plugin name="ui"/>
				
				<br/>
				<div style="width:900px;height:250px;border:1px solid silver;margin:auto;" align="center">
					<br /><br /><br /><i>flash goes here</i>
				</div>
				
				
				<g:if test="${flash.message}">
					<div class="message" style="width:75%">${flash.message}</div>
				</g:if>
				
				<div id="centerContent" class="welcomeTitle" style="margin:20px">
					
						
					<br/>
					<table border="0">
						<tr><td valign="top">
					<table class="listTable" border="1" style="font-size:.9em" id="patientsTable">
						<th colspan="2" style="padding:8px 8px 8px 8px;background-color:#BDD2FF">Patients</th>
						<tr style="padding:4px 4px 4px 4px">
							<td>Total</td>
							<td>${session.studyCounts['Total']}</td>
							<g:if test="${session.studyCounts}">
							<g:each in="${session.studyCounts}" var="item">
								<g:if test="${item.key != 'Total'}">
									<tr>
									<td>${item.key}</td>
									<td>${item.value}</td>
									</tr>
								</g:if>
							</g:each>
							</g:if>
							<g:else>
								<tr>
									<td>Error communicating with patient service.  Please try again.</td>
								</tr>
							</g:else>
						</tr>
					</table> 
					</td><td valign="top" style="padding-left:50px">
					<table class="sourceTable" id="samplesTable" style="font-size:.9em">
						<thead><tr>
						<th colspan="2" style="padding:8px 8px 8px 8px;background-color:#BDD2FF">Samples</th>
						</tr>
						</thead>
						
						<tbody>
						<g:if test="${session.anatomicSourceValues}">
								<g:each in="${session.anatomicSourceValues}" var="item">
									
												<tr>
												<td>${item.key}</td>
											    <td>${item.value}</td>
												</tr>
												
								</g:each>
						</g:if>
						<g:else>
							<tr>
							<td>Error communicating with sample service.  Please try again.</td>
							</tr>
						</g:else>
						<tbody>
					</table>
				</td></tr></table>
					
					
					
				</div>
				
				<script type="text/javascript">
						jQuery(document).ready(function() {
							$("#samplesTable").Scrollable(125, 250);
						});
				 	</script>
				
    </body>
</html>