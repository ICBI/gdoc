<html>
    <head>
        <title>Georgetown Database of Cancer</title>
		<meta name="layout" content="workflowsLayout" />
		<g:javascript library="jquery"/>
		<g:javascript src="jquery/scrollTable/scrolltable.js"/>
		<g:javascript src="jquery/scrollTable/jscrolltable.js"/>
		<!-- styling -->
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'scrollable-navig.css')}"/>
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'dialog.css')}"/>
		
    </head>
    <body>
				<jq:plugin name="ui"/>
				<jq:plugin name="autocomplete"/>
					<jq:plugin name="tooltip"/>
				<g:javascript>



				  $(document).ready(function(){
					$('.info').tooltip({showURL: false});
				   	$("#q").autocomplete("/gdoc/search/relevantTerms",{
							max: 130,
							scroll: true,
							multiple:false,
							matchContains: true,
							dataType:'json',
							parse: function(data){
								var array = jQuery.makeArray(data);
								for(var i=0;i<data.length;i++) {
				 					var tempValue = data[i];
									var tempResult = data[i];
									array[i] = { data:data[i], value: tempValue, result: tempResult};
							    }
								return array;
							},
				            formatItem: function(data, i, max) {
										return data;
									},

							formatResult: function(data) {
										return data;
									}
					});


				  });


				</g:javascript>
				<div style="width:85%">
			
				
				<g:if test="${flash.message}">
					<div class="message" style="width:75%">${flash.message}</div>
				</g:if>
				
				
				<div id="centerContent">
						<div style="text-align:center;margin-top:35px;margin-bottom:25px">
							<g:form autocomplete="off" controller="search" action="index">

							 <input name="q" id="q" type="text" value="" size="35"></input>

							<input type="submit" value="search gdoc" />
							</g:form>
							<span style="font-size:.8em;margin-top:8px;">(enter published findings<img class="info" title="info about finding here" src="${createLinkTo(dir:'images',file:'information.png')}" border="0" />
					 genes, proteins, cancer type, studies, investigators, authors ...)</span>
						</div>
						<br/>
						
						<!-- root element for scrollable -->
				</div>
				
				<div style="float:left;width:45%;padding-right:5px">
						<table border="0">
							<tr>
								<td colspan="2" style="height:29px;background: #00ff00 url('/gdoc/images/bgTitles.png') repeat;">
									<p style="margin-top:4px;color:#336699">Getting Started with G-DOC</p>
								</td>
							</tr>
								<tr>
									<td valign="top">
										<img src="${createLinkTo(dir:'images',file:'quickStart.png')}" border="0" />
									</td>
									<td valign="top" style="color:#336699;">
										<p style="font-size:1.1em;text-decoration:underline;padding-top:7px">
											<g:link controller="quickStart" style="color:#336699;">Quick Start</g:link></p>
										<p style="font-size:.8em">Begin using GDOC in a few steps. Search by cancer types (diseases), microarrays and the all data currently available.</p>
									</td>
								</tr>
								<tr>
									<td>
										<img src="${createLinkTo(dir:'images',file:'tutorialsIcon.png')}" border="0" />
									</td>
									<td style="color:#336699">
										<p style="font-size:1.1em;text-decoration:underline;padding-top:0px">
											<a href="#" style="color:#336699;">Tutorials</a></p>
										<p style="font-size:.8em">Watch step-by-step movies of workflows that are available within the G-DOC application. </p>
									</td>
								</tr>
						</table>
				</div>
				<div style="float:right;width:45%">
						<table border="0">
							<tr>
								<td colspan="2" style="height:29px;background: #00ff00 url('/gdoc/images/bgTitles.png') repeat;">
									<p style="margin-top:4px;color:#336699">Features</p>
								</td>
							</tr>
								<tr style="color:#336699;border-bottom:1px solid #336699;" valign="top">
									<td>
										<p style="font-size:1.1em;text-decoration:underline;padding-top:7px;padding-left:25px">Search</p>
										<img src="${createLinkTo(dir:'images',file:'searchIcon.png')}" border="0" />
									</td>
									<td>
										
											<p style="font-size:.8em;margin-top:20px">
											<g:link controller="clinical" style="color:#336699;">Clinical Data</g:link><br /><br />
											<g:link controller="genomeBrowser" style="color:#336699;">Browse Genome</g:link><br /><br />
											<g:link controller="moleculeTarget" style="color:#336699;">Compounds/Drug Targets</g:link> <br /><br />
											<g:link controller="geneExpression" style="color:#336699;">Gene Expression</g:link><br /><br />
											<g:link controller="studyDataSource" style="color:#336699;">Studies</g:link> <br /><br />
											<g:link controller="finding" style="color:#336699;">Findings</g:link> <br /><br />
											<span style="color:gray;text-decoration:underline">Biospecimens (future feature)</span><br />
											</p><br />
									</td>
								</tr>
								<tr style="color:#336699;border-bottom:1px solid #336699;" valign="top">
									<td>
										<p style="font-size:1.1em;text-decoration:underline;padding-top:0px;padding-left:22px">Analyze</p>
										<img src="${createLinkTo(dir:'images',file:'analysisIcon.png')}" border="0" />
									</td>
									<td style="padding-bottom:5px">
											<p style="font-size:.8em;margin-top:10px">
											<g:link controller="analysis" style="color:#336699;">Group Comparison</g:link> / 
											<g:link controller="km" style="color:#336699;">KM Plots</g:link><br /><br />
											<g:link controller="heatMap" style="color:#336699;">HeatMap Viewer</g:link><br /><br />
											<g:link controller="pca" style="color:#336699;">Classification</g:link><br /><br />
											<span style="color:gray;text-decoration:underline">Correlations/Multi-Omics (future feature)</span><br /><br />
											<span style="color:gray;text-decoration:underline">Pathways/Networks (future feature)</span><br />
											
											</p>
									</td>
								</tr>
								<tr style="color:#336699" valign="top">
									<td>
										<p style="font-size:1.1em;text-decoration:underline;padding-top:7px;padding-left:15px">My GDOC</p>
										
										<img src="${createLinkTo(dir:'images',file:'myGdoc.png')}" border="0" />
									</td>
									<td>
										<p style="font-size:.8em;margin-top:20px">
<g:link controller="notification" style="color:#336699;">Notifications</g:link><br /><br />
<g:link name="View My Saved Lists" controller="userList" style="color:#336699;">Saved Lists</g:link><br /><br />
<g:link name="View My Saved Analysis" controller="savedAnalysis" style="color:#336699;">Saved Analysis</g:link><br /><br />
<g:link name="Collaboration Groups" controller="collaborationGroups" style="color:#336699;x">Manage my groups / Request access</g:link>
		</p>
									</td>
								</tr>
						</table>
				</div>		
					
					
					
					
					
					
					
					<%--tr>
						<td valign="top" style="padding:8px;width:33%">
				<div class="dialog">
				 <div class="content">
				  <div class="t"></div>
				  <!-- Your content goes here -->
				  <h1 style="font-size:1.2em;border-bottom:2px solid white">Search</h1><br />
					<a style="color:#fff;padding:5px" href='#'>Biospecimens</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Clinical Data</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Browse Genome</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Compounds/Drug Targets</a> <br />
				 </div>
				 <div class="b"><div></div></div>
				
				
				</div>		
					</td>
					<td valign="top" style="padding:8px;width:33%">
			<div class="dialog3">
			 <div class="content">
			  <div class="t"></div>
			  <!-- Your content goes here -->
			  	<h1 style="font-size:1.2em;border-bottom:2px solid white">Analyze</h1><br />
					<a style="color:#fff;padding:5px" href='#'>Group Comparison</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Classification</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Correlations/Multi Omics</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Pathways/Networks</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Advanced Molecular Analysis (with Gene Pattern)</a><br />
			 </div>
			 <div class="b"><div></div></div>
			</div>		
				</td>
				<td valign="top" style="padding:8px;width:34%">
		<div class="dialog2">
		 <div class="content">
		  <div class="t"></div>
		  <!-- Your content goes here -->
		  	<h1 style="font-size:1.2em;border-bottom:2px solid white">My G-DOC</h1><br />
				<g:link controller="notification" style="color:#fff;padding:5px">Notifications</g:link><br /><br />
				<g:link name="View My Saved Lists" controller="userList" style="color:#fff;padding:5px">Saved Lists</g:link><br /><br />
				<g:link name="View My Saved Analysis" controller="savedAnalysis" style="color:#fff;padding:5px">Saved Analysis</g:link><br /><br />
				<g:link name="Collaboration Groups" controller="collaborationGroups" style="color:#fff;padding:5px">Collaboration Groups</g:link>
					<ul style="margin-left:15px"><br />
						<li>-<g:link controller="collaborationGroups" style="color:#fff;padding:5px">Manage my groups</g:link></li><br />
						<li>-<g:link controller="collaborationGroups" params="[requestGroupAccess:true]" style="color:#fff;padding:5px">Request Access</g:link></li>
					</ul>
				<br />
				<br />
				
		 </div>
		 <div class="b"><div></div></div>
		</div>		
			</td>
					</tr--%>
					
					
					<br/>
					</div>
    </body>
</html>