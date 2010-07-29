
<g:javascript>
jQuery(document).ready(function()
{
	jQuery("ul.sf-menu").superfish({ 
		animation: {height:'show'},   // slide-down effect without fade-in 
		delay:     1200               // 1.2 second delay on mouseout 
	});
	jQuery('.genePatternLink').click(function() {
		jQuery('#gpForm').submit();
		return false;
	})
});
</g:javascript>
<jq:plugin name="hoverIntent"/>
<jq:plugin name="superfish"/>
<div id="navigation-block">
	<ul id="gdocNavigation" class="sf-menu sf-vertical sf-js-enabled sf-shadow">
		<li>
			<g:navigationLink name="Home" controller="workflows" />
		</li>
		<li>
			<a class="sf-with-ul" href="#">Search<span class="sf-sub-indicator"> »</span></a>
			<ul style="display: none; visibility: hidden;">
				<li>
					<%--a href="${createLink(controller: 'sample')}">Biospecimens</a--%>
					<a href="${createLink(controller: 'clinical')}">Clinical Data</a>
					<a href="#">Browse Genome</a>
					<g:link controller="geneExpression">Gene Expression</g:link>
					<a href="${createLink(controller: 'moleculeTarget')}">Compounds and Targets</a>
					<a href="${createLink(controller: 'studyDataSource')}">Studies</a>
					<a href="${createLink(controller: 'finding')}">Findings</a>
				</li>		
			</ul>
		</li>
		<li>
			<a class="sf-with-ul" href="#">Analyze<span class="sf-sub-indicator"> »</span></a>
			<ul style="display: none; visibility: hidden;">
					<li>
						<a href="${createLink(controller: 'analysis')}">Group Comparison / KM Plots</a>
						<a href="${createLink(controller: 'pca')}">Classification</a>
						<%--a href="#">Correlations/Multi Omics</a--%>
						<%--a href="#">Pathways and Networks</a--%>
						<g:link controller="heatMap">HeatMap Viewer</g:link>
					</li>
			</ul>
		<li>
			<a href="https://demotisu.gdoc.georgetown.edu/catissuecore" target="_blank">Tissue Banking (caTissue)</a>
		</li>
		
		<li>
			<a href="#">Help</a>
		</li>		
	</ul>
	<form id="gpForm" action="${grailsApplication.config.genePatternUrl}/gp/pages/index.jsf" method="POST" target="genepattern">
		<input type="hidden" name="workspaceId" value="${genePatternId()}" />
	</form>
</div>

<div id="navigation-block">
	<ul id="mygdocNavigation" class="sf-menu sf-vertical sf-js-enabled sf-shadow">
		<li>
			<g:link controller="notification">Notifications</g:link>
		</li>
		<li>
			<g:link name="View My Saved Lists" controller="userList">Saved Lists</g:link>
		</li>
		<li>
			<g:link name="View My Saved Analysis" controller="savedAnalysis">Saved Analysis</g:link>
		</li>
		<li>
			<g:link name="Collaboration Groups" controller="collaborationGroups">Manage my groups / Request access</g:link>
		</li>
</div>







