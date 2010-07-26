<html>
    <head>
        <title>Georgetown Database of Cancer</title>
		<g:render template="/common/flex_header"/>
		<meta name="layout" content="splash" />
		<g:javascript library="jquery"/>
		<g:javascript src="jquery/scrollTable/scrolltable.js"/>
		<g:javascript src="jquery/scrollTable/jscrolltable.js"/>
		<jq:plugin name="tooltip" />
		<jq:plugin name="curvycorners"/>
		<!-- styling -->
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'scrollable-navig.css')}"/>
		<g:javascript>
			$(document).ready(function(){
						$("[class*='info']").each(function(index){
							$(this).tooltip({showURL: false});
						});
						
						$("[class*='sel']").each(function(index){
							$(this).css('cursor','pointer');
							var part = $("[class*='parts']").eq(index);
							$(this).click(function() {
								
							  	$("[class*='parts']").each(function(index){
								 $(this).css('display','none');
								});
								$("[class*='sel']").each(function(index){
								 $(this).css('border-right','1px solid #334477');
								 $(this).css('background-color','#EBF1FF');
								});
							  	$(this).css('background-color','#fff');
								$(this).css('border-right','0px');
							  	part.css('display','block');
							});
							$(this).hover(function(){
								var color = $(this).css('background-color');
								if(color == 'rgb(255, 255, 255)'){
									$(this).css('background','#fff');
								}else{
									$(this).css('background','#EBF1FF');
								}
							});
							$(this).mouseleave(function(){
								var color = $(this).css('background-color');
								if(color == 'rgb(255, 255, 255)'){
									$(this).css('background','#fff');
								}else{
									$(this).css('background','#EBF1FF');
								}
								
							});
							
						});
						$('.partDiv').corner();
						
			});
		
		</g:javascript>
		
    </head>
    <body>
				<jq:plugin name="ui"/>
				
				<br/>
				<div style="width:900px;height:350px;border:1px solid silver;margin:auto;" align="center">
					<g:flex component="Main" width="900px" height="350px" />
				</div>
				
				
				<g:if test="${flash.message}">
					<div class="message" style="width:75%">${flash.message}</div>
				</g:if>
				
				<div id="centerContent" class="welcomeTitle" style="margin:20px">
					
						
					<br/>
					
					<table border="0" style="width:900px;margin:auto">
						<tr>
							<td valign="top" style="width:45%">
							<table class="listTable" border="1" style="font-size:.9em" id="patientsTable">
								<th colspan="4" style="padding:8px 8px 8px 8px;background-color:#EBF1FF">Cancer/Study Overview</th>
								<tr style="padding:4px 4px 4px 4px;background-color:#f2f2f2">
									<td>Disease</td>
									<td># Studies</td>
									<td># Patients</td>
									<td>Available Data Types</td>
									<g:if test="${diseaseBreakdown}">
									<g:each in="${diseaseBreakdown}" var="item">
											<tr>
											<td>${item.key}</td>
											<td>${item.value.studyNumber}</td>
											<td>${item.value.patientNumber}</td>
											<td>
												<g:each in="${item.value.availableData}" var="nameAndImage">
													<g:each in="${nameAndImage}" var="n">
														<img src="/gdoc/images/${n.value}" alt="${n.key}" class="info" title="${n.key}" />
													</g:each>
												</g:each>
											</td>
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
							</td>
							<td valign="top" rowspan="2" style="width:450px">
									<div style="display:block;margin-left:30px;">
										<table border="0">
											<tr>
											<td style="width:10%" valign="top">
												<div class="sel" style="padding:25px;border-top:1px solid #334477;border-right:0px;border-left:1px solid #334477;background-color:#fff">FINDINGS</div>
												<div class="sel" style="padding:25px;border-top:1px solid #334477;border-right:1px solid #334477;border-left:1px solid #334477;background-color:#EBF1FF">NEWS</div>
												<div class="sel" style="padding:25px;border-top:1px solid #334477;border-right:1px solid #334477;border-left:1px solid #334477;background-color:#EBF1FF;border-bottom:1px solid #334477;">PUBLICATIONS</div>
												<div style="height:150px;border-right:1px solid #334477"></div>
											</td>

											<td rowspan="3" valign="top">
												<div id="findings" class="parts" style="padding:15px;border-bottom:1px solid #334477;height:330px;border-right:0px solid #334477;height:330px;border-top:1px solid #334477;height:330px;;overflow: scroll;">
													<div class="partDiv">
													<g:if test="${findings}">
													<g:each in="${findings}" var="finding">
														<p style="border-bottom:1px dashed black;padding:2px">${finding.title}&nbsp;</p>
													</g:each>
													</g:if>
													</div>
												</div>
												
												<div id="news" class="parts" style="padding:15px;border-bottom:1px solid #334477;height:330px;border-right:1px solid #334477;height:330px;border-top:1px solid #334477;height:330px;overflow: scroll;display:none">
													<div class="partDiv">
													<g:if test="${feedMap}">
													<g:each in="${feedMap}" var="feedItem">
														<p><a href="${feedItem.value}" target="_blank">${feedItem.key}</a></p>
													</g:each>
													</g:if>
													</div>
												</div>
												
												<div id="pub" class="parts" style="padding:15px;border-bottom:1px solid #334477;height:330px;border-right:1px solid #334477;height:330px;border-top:1px solid #334477;height:330px;overflow: scroll;display:none">
													<div class="partDiv">
													future publications will be listed here...
													</div>
												</div>
											</td>
											</tr>



										</table>

									</div>
							</td>
						</tr>
						<tr>
							<td valign="top" style="padding-top:15px">
							<table class="listTable" border="1" style="font-size:.9em" id="patientsTable">
								<th colspan="4" style="padding:8px 8px 8px 8px;background-color:#EBF1FF">Data-Type Overview</th>
								<tr style="padding:4px 4px 4px 4px;background-color:#f2f2f2">
									<td>Data Type</td>
									<td># Studies</td>
									<g:if test="${dataBreakdown}">
									<g:each in="${dataBreakdown}" var="data">
											<tr>
											<td><div valign="top" style="text-align:top">${data.key} &nbsp;&nbsp;<img src="/gdoc/images/${data.key.replace(" ","_")}_icon.gif" alt="${data.key}" style="margin-bottom:-5px" class="info" title="${data.key}"></div></td>
											<td>${data.value}</td>
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
							</td>
								
						</tr>	
						</td>
					
						
						</tr>
						
						
						</table>	
							
							<script type="text/javascript">
									jQuery(document).ready(function() {
										$("#samplesTable").Scrollable(125, 250);
									});
							 	</script>
					
							
							
						<%--td valign="top">
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
					</table--%>
				
					
					
					
				</div>
				
				
				
    </body>
</html>