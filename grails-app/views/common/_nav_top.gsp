<g:javascript>
jQuery(document).ready(function()
{
	jQuery("ul.sf-menu").superfish({ 
		animation: {height:'show'},   // slide-down effect without fade-in 
		delay:     1200               // 1.2 second delay on mouseout 
	});
});
</g:javascript>
<jq:plugin name="hoverIntent"/>
<jq:plugin name="superfish"/>

<div id="top-navigation-block" width="100%">
	<ul id="gdocNavigation" class="sf-menu sf-vertical sf-js-enabled sf-shadow">
		<li>
			<g:navigationLink name="Home" controller="workflows" />
		</li>
		<li>
			<a class="sf-with-ul" href="#">Search<span class="sf-sub-indicator"> »</span></a>
			<ul style="display: none; visibility: hidden;">
				<li>
					<a href="${createLink(controller: 'sample')}">Biospecimens</a>
					<a href="${createLink(controller: 'clinical')}">Clinical Data</a>
					<a href="${createLink(controller: 'genomeBrowser')}">Browse Genome</a>
					<g:link controller="geneExpression">Gene Expression</g:link>
					<a href="${createLink(controller: 'moleculeTarget')}">Compounds and Targets</a>
					<a href="${createLink(controller: 'studyDataSource')}">Studies</a>
				</li>		
			</ul>
		</li>
		<li>
			<a class="sf-with-ul" href="#">Analyze<span class="sf-sub-indicator"> »</span></a>
			<ul style="display: none; visibility: hidden;">
					<li>
						<a href="${createLink(controller: 'analysis')}">Group Comparison / KM Plots</a>
						<a href="${createLink(controller: 'pca')}">Classification</a>
						<a href="${createLink(controller: 'genomeBrowser')}">Correlations/Multi Omics</a>
						<a href="#">Pathways and Networks</a>
						<g:link controller="heatMap">HeatMap Viewer</g:link>
					</li>
			</ul>
		
		<li><g:link controller="userList">Saved Lists</g:link></li>
		<li><g:navigationLink name="Saved Analysis" controller="savedAnalysis">Saved Analysis</g:navigationLink></li>
		<li>
			<a href="#">Help</a>
		</li>		
	</ul>

</div>