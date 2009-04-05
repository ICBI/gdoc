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
<jq:plugin name="hoverIntent"/>
<div id="navigation-block">
	<ul id="gdocNavigation" class="sf-menu sf-vertical sf-js-enabled sf-shadow">
		<li>
			<g:navigationLink name="Home" controller="home"/>
		</li>
		<li>
			<a class="sf-with-ul" href="#">Search for Data<span class="sf-sub-indicator"> »</span></a>
			<ul style="display: none; visibility: hidden;">
				<li>
					<g:navigationLink name="Clinical" controller="clinical" class="submenu"/>
				</li>
				<li>
					<g:navigationLink name="Genomic" controller="genomic" />
				</li>				
			</ul>
		</li>
		<li>
			<g:navigationLink name="Perform Analysis" controller="analysis"/>
		</li>	
		<li>
			<a href="#">Help</a>
		</li>		
	</ul>

</div>







