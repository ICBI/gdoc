<g:javascript>
$(document).ready(function()
{
	$("ul.sf-menu").superfish({ 
		animation: {height:'show'},   // slide-down effect without fade-in 
		delay:     1200               // 1.2 second delay on mouseout 
	});
});
</g:javascript>
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
					<a class="sf-with-ul" href="#">Studies<span class="sf-sub-indicator"> »</span></a>
					<ul style="display: none; visibility: hidden;">
						<g:each in="${session.myStudies}">
							<li>
								<g:navigationLink name="${it.shortName}" id="${it.id}" controller="clinical" />
							</li>
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
					<a href="#">Targets</a>
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
					<g:navigationLink name="Class Comparison" controller="analysis"/>
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
			</ul>			
			
		</li>	
		<li>
			<a href="#">Help</a>
		</li>		
	</ul>

</div>