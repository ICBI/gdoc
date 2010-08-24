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
	<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all">
		<div class="ui-jqgrid-view">
			<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-tl ui-corner-tr ui-helper-clearfix">
				<a id="analysisDetailsCollapse" href="#" class="ui-jqgrid-titlebar-close">
					<span id="detailsIcon" class="ui-icon ui-icon-circle-triangle-n"></span>
				</a>
				<span class="ui-jqgrid-title">Analysis Results</span>
			</div>
		</div>
	</div>
	<div id="analysisTableDetails" >
	<table width="100%" >
		<g:each in="${it.query}" var="data">
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
					<td class="ui-widget-content" id="${data.key.decamelize()}" style="padding: 2px;" >${data.value}</td>
				</g:else>
			</tr>
		</g:each>
	</table>
	</div>
</div>