<html>
    <head>
        <title>Georgetown Database of Cancer</title>
		<meta name="layout" content="main" />
		<g:javascript library="jquery"/>
		<g:javascript src="jquery/scrollTable/scrolltable.js"/>
		<g:javascript src="jquery/scrollTable/jscrolltable.js"/>
		<!-- styling -->
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'scrollable-navig.css')}"/>
		
		
    </head>
    <body>
				<jq:plugin name="ui"/>
				
				<br/>
			
				<div style="font-size:16px;margin-bottom:5px">Welcome to GDOC</div>
				<div id="centerContent" class="welcomeTitle">
						<p>The Georgetown Database of Cancer integrates multiple datatypes to present
						a unified data view, allowing for rapid data exploration.</p>
						
						<p>Lombardi's world-renowned Research Faculty are discovering cancer
							 risk factors, designing effective prevention strategies, and learning
							 how to detect cancers earlier. They are developing and testing the cancer
							 treatments of tomorrow - targeted therapies that will improve both survival
							 and quality of life.</p>
						<br/>
						<!-- root element for scrollable -->
						
						
					<br/>
					<table>
						<tr><td valign="top">
					<table class="listTable" border="1" style="font-size:.9em" id="patientsTable">
						<th colspan="2" style="padding:8px 8px 8px 8px;background-color:#BDD2FF">Patients</th>
						<tr style="padding:4px 4px 4px 4px">
							<td>Total</td>
							<td>${session.patientSummary}</td>
							<g:if test="${session.studyCounts}">
							<g:each in="${session.studyCounts}" var="item">
								<tr>
								<td>${item.key}</td>
								<td>${item.value}</td>
								</tr>
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