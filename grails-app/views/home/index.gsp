<html>
    <head>
        <title>Georgetown Database of Cancer</title>
		<meta name="layout" content="main" />
		
    </head>
    <body>
				<jq:plugin name="ui"/>
				<script type="text/javascript">
				$(document).ready(function(){
				  $("#centerTabs").tabs();
				});
				</script>
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
					<br/>
					<div class="tabDiv">
						<div id="centerTabs" class="tabDiv">
						    <ul>
						        <li><a href="#fragment-4"><span>Patient Metrics</span></a></li>
						        <li><a href="#fragment-5"><span>Study Metrics</span></a></li>
						 				<li><a href="#fragment-6"><span>Sample Metrics</span></a></li>
						    </ul>
						    <div id="fragment-4">
						        Total number of patients across studies: ${session.patientSummary}<br />
								<br /><br /><br /><br /><br /><br />
						    </div>
						    <div id="fragment-5">
							        Total number of studies: ${session.studySummary}<br />
									<br /><br /><br /><br /><br /><br />
						    </div>
								<div id="fragment-6">
									<table class="summaryTable">
										<tr>
											<th>Datasource</th>
											<th>Sample Count</th>
										</tr>
										<g:each in="${session.sampleSummary}" var="item">
											<tr>
												<td>${item.key}</td>
												<td style="padding: 0px;">
													<table>
														<g:each in="${item.value}" var="data">
															<tr>
																<td>${data.key.decamelize()}</td>
																<g:each in="${data.value}" var="value">
																	<g:each in="${value}">
																		<td>${it.key} : ${it.value}</td>
																	</g:each>
																</g:each>
															<tr>
														</g:each>
													</table>
												</td>
											</tr>
										</g:each>
									</table>
						    </div>
						</div>
					</div>
				</div>
    </body>
</html>