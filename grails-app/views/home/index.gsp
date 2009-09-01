<html>
    <head>
        <title>Georgetown Database of Cancer</title>
		<meta name="layout" content="main" />
		<g:javascript src="tools.scrollable-1.0.5.js"/>
		<script src="http://static.flowplayer.org/js/jquery.mousewheel.js"></script>
		
		<!-- styling -->
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'scrollable-navig.css')}"/>
		
		
    </head>
    <body>
				<jq:plugin name="ui"/>
				
				<script type="text/javascript">
				$(document).ready(function(){
				  $("#centerTabs").tabs();
				  $("div.scrollable").scrollable();
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
						<!-- root element for scrollable -->
						
						
					<br/>
					<div class="tabDiv">
						<div id="centerTabs" class="tabDiv" style="margin-bottom:20px;">
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
								<div id="fragment-6" style="padding: 0px; margin: 0px;">
									<g:if test="${session.sampleSummary}">
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
																
																	<td>
																		
																	<g:if test="${data.value.size()>=3}">
																			<!-- prev link --> 
																			<a class="prev"></a> 
																	</g:if>
																			<!-- root element for scrollable --> 
																			<div class="scrollable">
																			<!-- root element for the items --> 
																				 <div class="items">
																			   <g:each in="${data.value}" var="value">
																						<g:each in="${value}">
																						<div>
																						<table border="1"><tr><td>
																							${it.key} :
																						</td>
																						<td>
																						 ${it.value}
																						</td>
																						</tr>
																						</table>
																						</div>
																						</g:each>
																				</g:each>
																				</div>	
																			</div>
																			<g:if test="${data.value.size()>=3}">
																			<!-- next link -->
																			<a class="next"></a>
																			</g:if>
																					
																	</td>
															
															<tr>
														</g:each>
													</table>
												</td>
											</tr>
										</g:each>
									</table>
									</g:if>
									<g:else>
										<br/>
										Error communicating with sample service.  Please try again.
										<br/>
										<br/>
										<br/>
										<br/>
									</g:else>
						    </div>
						</div>
					</div>
				</div>
    </body>
</html>