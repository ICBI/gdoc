<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
		</script>
        <title>Search Clinical Data</title>         
    </head>
    <body>
		<jq:plugin name="tooltip"/>
		<jq:plugin name="ui"/>
		<g:javascript>
			$(document).ready(function (){
				$('.info').tooltip({showURL: false});
				$('.close').click(function() {
					$(this).parents('.clinicalSearch').hide('slow');
				});
				$('.slider-range').each(function() {
					var upper = parseInt(jQuery(this).data("maxval"));
					var lower = parseInt(jQuery(this).data("minval"));
					var rangeInput = jQuery(this).parents('div').children('.rangeValue');
					if(rangeInput.value) {
						var values = rangeInput.value.split(' - ');
					} else {
						rangeInput.val(lower + ' - ' + upper);
					}
					jQuery(this).slider({
						range: true,
						min: lower,
						max: upper,
						values: [lower, upper],
						slide: function(event, ui) {
										rangeInput.val(ui.values[0] + ' - ' + ui.values[1]);
						}
					});
				});
			});

		</g:javascript>
	<p style="font-size:14pt">Search ${session.study.shortName} Clinical Data</p>
	<div id="centerContent">
		<br/>
		<g:form name="searchForm" action="search">
			<g:each in="${session.dataTypes}">
				<g:if test="${it.target == 'PATIENT'}">
					<g:set var="type" value="clinical"/>
				</g:if>
				<g:else>
					<g:set var="type" value="biospecimen" />
				</g:else>
				<div class="clinicalSearch">
					<div style="float: left">
						${it.longName} 	
						<img class="info" title="${it.definition}" src="${createLinkTo(dir:'images',file:'information.png')}" border="0" />
					</div>
				<div style="float: right; vertical-align: middle">
					<img class="close"src="${createLinkTo(dir:'images',file:'cross.png')}" border="0" />
				</div>
				<br/>
				<br/>
					<g:if test="${it.vocabulary}">
						<div align="left">
							<g:select name="${type + '_' + it.shortName}" 
									noSelection="${['':'Select One...']}"
									from="${it.vocabs}" optionKey="term" optionValue="termMeaning">
							</g:select>
						</div>
						<br/>
					</g:if>
					<g:elseif test="${it.qualitative}">
						<g:textField name="${type + '_' + it.shortName}"  />
						<br/>
					</g:elseif>
					<g:elseif test="${it.lowerRange != null && it.upperRange != null}">
						<div align="center">
							<label for="rangeValue" style="padding-left: 130px">Range:</label>
							<g:textField name="${type + '_range_' + it.shortName}" class="rangeValue" style="border:0; font-weight:bold; background: #E6E6E6;"  />
							<br/>
							<br/>
							<table>
								<tr>
									<td style="padding-right: 10px">${it.lowerRange}</td>
									<td>
										<div class="slider-range" style="width: 250px"></div>
									</td>
									<td style="padding-left: 10px">${it.upperRange}</td>
								</tr>
							</table>
						</div>
						<script type="text/javascript">
							var item = jQuery('.slider-range').get(jQuery('.slider-range').size() - 1); 
							jQuery(item).data("minval", "${it.lowerRange}");
							jQuery(item).data("maxval", "${it.upperRange}");
						</script>
						<br/>
					</g:elseif>
					<g:else>
						Between <g:textField name="${type + '_' + it.shortName}"  /> and <g:textField name="${type + '_' + it.shortName}"  />
					</g:else>
				</div>
			</g:each>
			<br/>
			<br/>
			<g:submitButton name="submit" value="Search"/>
		</g:form>
	</div>
	</body>
	
</hmtl>