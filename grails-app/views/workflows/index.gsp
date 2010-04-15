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
				
				<br/>
			
				
				<g:if test="${flash.message}">
					<div class="message" style="width:75%">${flash.message}</div>
				</g:if>
				
				<div id="centerContent">
						<p><span style="font-size:1.1em">G-DOC</span> allows you to search for patients using clinical parameters -- or search samples, drugs, compounds, genes and findings. Analyze genomic expression, copy number, microRNA and other "-omics" data in relation to patient cohorts. By using the translational capabilities within this system, we hope you discover relationships among disparate datasets across G-DOC and collaborate with other researchers. Select from the options below to begin a workflow, or manage your data and account status.
						</p>
						<br/>
						<!-- root element for scrollable -->
				</div>
				
				<table style="width:100%;">
					<tr>
						<td valign="top">
				<div class="dialog">
				 <div class="content" style="padding-right:40%">
				  <div class="t"></div>
				  <!-- Your content goes here -->
				  <h1 style="font-size:1.2em;border-bottom:2px solid white">Search</h1><br />
					<a style="color:#fff;padding:5px" href='#'>G-DOC&nbsp;Findings</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Sample data</a><br />
					<br />
					<p style="color:#fff;padding:5px">Patient data
				  	<ul>
						<g:each in="${session.myStudies}">
						<g:if test="${it.hasClinicalData()}">
							<li>
								&nbsp;&nbsp;&nbsp;<g:navigationLink name="${it.shortName}" id="${it.id}" controller="clinical" />
							</li>
						</g:if>
						</g:each>
					</ul>
					<br />
					</p>
					<a style="color:#fff;padding:5px" href='#'>Genes</a> <br /><br />
					<a style="color:#fff;padding:5px" href='#'>Compounds</a> <br />
				 </div>
				 <div class="b"><div></div></div>
				
				
				</div>		
					</td>
					<td valign="top">
			<div class="dialog3">
			 <div class="content" style="padding-right:40%">
			  <div class="t"></div>
			  <!-- Your content goes here -->
			  	<h1 style="font-size:1.2em;border-bottom:2px solid white">Analyze</h1><br />
					<a style="color:#fff;padding:5px" href='#'>Class&nbsp;Comparison</a><br />
					<g:each in="${session.myStudies}">
						<g:if test="${it.hasGenomicData()}">
							<li>
								&nbsp;&nbsp;&nbsp;<g:navigationLink name="${it.shortName}" id="${it.id}" controller="analysis" />
							</li>
						</g:if>
					</g:each>
					<br />
					<a style="color:#fff;padding:5px" href='#'>Copy Number</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>KM Plot</a><br />
						<g:each in="${session.myStudies}">
						 <g:if test="${it.hasGenomicData()}">
							<li>
								&nbsp;&nbsp;&nbsp;<g:navigationLink name="${it.shortName}" id="${it.id}" controller="analysis" />
							</li>
						</g:if>
						</g:each>
					<br />
					<a style="color:#fff;padding:5px" href='#'>PCA</a><br /><br />
					<a style="color:#fff;padding:5px" href='#'>Gene Pattern</a><br />
			 </div>
			 <div class="b"><div></div></div>
			</div>		
				</td>
				<td valign="top">
		<div class="dialog2">
		 <div class="content" style="padding-right:25%">
		  <div class="t"></div>
		  <!-- Your content goes here -->
		  	<h1 style="font-size:1.2em;border-bottom:2px solid white">My G-DOC</h1><br />
				<g:link controller="notification" style="color:#fff;padding:5px">Notifications</g:link><br /><br />
				<g:link name="View&nbsp;My&nbsp;Saved&nbsp;Lists" controller="userList" style="color:#fff;padding:5px">Saved Lists</g:link><br /><br />
				<g:link name="View&nbsp;My&nbsp;Saved&nbsp;Analysis" controller="savedAnalysis" style="color:#fff;padding:5px">Saved Analysis</g:link><br /><br />
				<g:link name="Collaboration&nbsp;Groups" controller="collaborationGroups" style="color:#fff;padding:5px">Collaboration Groups</g:link>
					<ul style="margin-left:15px"><br />
						<li>-<g:link controller="collaborationGroups" style="color:#fff;padding:5px">Manage&nbsp;my&nbsp;groups</g:link></li><br />
						<li>-<g:link controller="collaborationGroups" params="[requestGroupAccess:true]" style="color:#fff;padding:5px">Request&nbsp;Access</g:link></li>
					</ul>
				</li>
				<br /><br /><br /><br />
		 </div>
		 <div class="b"><div></div></div>
		</div>		
			</td>
					</tr>
					
					</table>	
					<br/>
					
    </body>
</html>