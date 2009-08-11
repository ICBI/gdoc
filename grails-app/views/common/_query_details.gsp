<g:javascript>
	jQuery(document).ready(function(){
		jQuery("#analysisDetailsCollapse").click(function(ev) {
			jQuery("#analysisTableDetails").slideToggle();
			jQuery("#detailsIcon").toggleClass("ui-icon-circle-triangle-s").toggleClass("ui-icon-circle-triangle-n");
			ev.preventDefault();
		});
	});
</g:javascript>
<div class="ui-widget" style="font-size: .8em; width: 250px">
	<table class="ui-jqgrid-htable" width="100%" >
		<tr class="ui-widget-header">
			<th style="padding: 2px">
				Query Details
			</th>
			<th>
				<div align="right">
				<a id="analysisDetailsCollapse" class="ui-jqgrid-titlebar-close" href="#">
					<span id="detailsIcon" class="ui-icon ui-icon-circle-triangle-n"/>
				</a>
				</div>
			</th>
		</tr>
	</table>
	<div id="analysisTableDetails" >
	<table width="100%" >
		<g:each in="${it}" var="data">
		<g:if test="${data.value}">
			<tr>
				<td class="ui-state-default ui-th-column" style="padding: 2px; border: 1px solid #CCCCCC">${data.key.decamelize()}</td>
				<g:if test="${data.value instanceof List}">
					<td class="ui-widget-content" style="padding: 2px;" >
						<g:collect in="${data.value}" expr="it">
						     ${it}<br/>
						</g:collect>
					</td>
				</g:if>
				<g:else>
					<td class="ui-widget-content" style="padding: 2px;" >
							${data.value}
					</td>
				</g:else>
			</tr>
			</g:if>
		</g:each>
	</table>
	</div>
</div>