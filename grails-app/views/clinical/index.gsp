<html>
    <head>
        <meta name="layout" content="main" />
		<meta http-equiv="cache-control" content="no-cache"> <!-- tells browser not to cache -->
		<meta http-equiv="expires" content="0"> <!-- says that the cache expires 'now' -->
		<meta http-equiv="pragma" content="no-cache"> <!-- says not to use cached stuff, if there is any -->
		<g:javascript library="jquery" />
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
					var lowerVal = lower;
					var upperVal = upper;
					var rangeInput = jQuery(this).parents('div').children('.rangeValue');
					if(rangeInput.val()) {
						var values = rangeInput.val().split(' - ');
						lowerVal = values[0];
						upperVal = values[1];
					} else {
						rangeInput.val(lower + ' - ' + upper);
					}
					jQuery(this).slider({
						range: true,
						min: lower,
						max: upper,
						values: [lowerVal, upperVal],
						slide: function(event, ui) {
										rangeInput.val(ui.values[0] + ' - ' + ui.values[1]);
						}
					});
				});
				
				$(":input").each(function(index){
					$(this).keypress(function(e){
			      		if(e.which == 13){
							//alert($('#searchForm') + "  submit");
							$(this).blur();
							e.preventDefault();
			       			$('#submit').focus().click();
							
			       		}
			      	});
				});
					
				
			});
			
			
		</g:javascript>
	<p style="font-size:14pt">Search for Clinical Data</p>
		
		<div id="studyPicker">
			<g:render template="/studyDataSource/studyPicker"/>
		</div>
		
	<div id="centerContent">
		
		<g:if test="${flash['errors']}">
			<br/>
				<div class="errorDetail">
					<ul>
				<g:each in="${flash['errors']}">
					<li style="list-style-type: disc"><g:message code="${it.value['message']}" args="${it.value['field']}" /></li>
				</g:each>
					</ul>
				</div>
			<br/>
			<br/>
		</g:if>
				
		
		
		
		<div id="searchDiv">
			<g:render template="/clinical/studyForm"/>
		</div>
		
		
	</div>
	</body>
	
</hmtl>