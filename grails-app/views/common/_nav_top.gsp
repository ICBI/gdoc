<g:javascript>
$(document).ready(function()
{
	$("ul.sf-menu").superfish({ 
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
			<g:navigationLink name="Home" controller="home"/>
		</li>
		<li>
			<a class="sf-with-ul" href="#">Search for Data<span class="sf-sub-indicator"> »</span></a>
			<ul style="display: none; visibility: hidden;">
				<li>
					<a class="sf-with-ul" href="${createLink(controller: 'studyDataSource')}">Studies<span class="sf-sub-indicator"> »</span></a>
					<ul style="display: none; visibility: hidden;">
						<g:each in="${session.myStudies}">
							<g:if test="${it.hasClinicalData()}">
							<li>
								<g:navigationLink name="${it.shortName}" id="${it.id}" controller="clinical" />
							</li>
							</g:if>
						</g:each>
					</ul>
				</li>
				<li>
					<a href="#">Patients</a>
				</li>	
				<li>
					<a href="#">Samples</a>
				</li>
				<li>
					<g:navigationLink name="Targets" controller="moleculeTarget" action="index" />
				</li>					
				<li>
					<g:navigationLink name="Genes" controller="geneExpression" />
				</li>						
			</ul>
		</li>
		<li>
			<a class="sf-with-ul" href="#">Perform Analysis<span class="sf-sub-indicator"> »</span></a>
			<ul style="display: none; visibility: hidden;">
				<li>
					<a class="sf-with-ul" href="#">Class Comparison<span class="sf-sub-indicator"> »</span></a>
					<ul style="display: none; visibility: hidden;">
					<g:each in="${session.myStudies}">
						<g:if test="${it.genomicData}">
							<li>
								<g:navigationLink name="${it.shortName}" id="${it.id}" controller="analysis" />
							</li>
						</g:if>
					</g:each>
					</ul>
				</li>
				<li>
					<a class="sf-with-ul" href="#">KM Plot<span class="sf-sub-indicator"> »</span></a>
					<ul style="display: none; visibility: hidden;">
						<g:each in="${session.myStudies}">
							<li>
								<g:navigationLink name="${it.shortName}" id="${it.id}" controller="km" />
							</li>
						</g:each>
					</ul>
				</li>
				<li>
					<a class="sf-with-ul" href="#">PCA<span class="sf-sub-indicator"> »</span></a>
					<ul style="display: none; visibility: hidden;">
						<g:each in="${session.myStudies}">
							<g:if test="${it.hasGenomicData()}">
								<li>
									<g:navigationLink name="${it.shortName}" id="${it.id}" controller="pca" />
								</li>
							</g:if>
						</g:each>
					</ul>
				</li>		
			</ul>			
			
		</li>
		<li><a href="/gdoc/userList" name="Saved Lists">Saved Lists</a></li>
		<li><g:navigationLink name="Saved Analysis" controller="savedAnalysis">Saved Analysis</g:navigationLink></li>	
		<li>
			<a href="#">Help</a>
		</li>		
	</ul>

</div>