<html>
    <head>
        <meta name="layout" content="report" />
        <title>CIN Results</title> 
		<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styles.css')}"/>
		<g:javascript library="jquery"/>
		<g:javascript library="jquery-1.3.2.min.js"/>
        <jq:plugin name="ui"/>
		<g:javascript>
		jQuery(document).ready(function() {

			jQuery("#cin-heatmap-container AREA").mouseover(function(){
				var chrMap = '.'+$(this).attr('id')+'-map';
				var chrList = '.'+$(this).attr('id')+'-list';
				jQuery(chrMap).css('display', 'inline');

				// Check if a click event has occurred and only change the chr hover state accordingly
				if (! jQuery('#cin-cytobands-container ul').hasClass('selected')) {
					jQuery(chrList).css('display', 'inline');
				}
			}).mouseout(function(){
				var chrMap = '.'+$(this).attr('id')+'-map';
				var chrList = '.'+$(this).attr('id')+'-list';

				// Check if a click event has occurred and only change the chr hover state accordingly
				if (! jQuery(chrMap).hasClass('selected')) {
					jQuery(chrMap).css('display', 'none');
				}

				// Check if a click event has occurred and only change the chr hover state accordingly
				if (! jQuery('#cin-cytobands-container ul').hasClass('selected')) {
					jQuery(chrList).css('display', 'none');
				}
			});

			jQuery("#cin-heatmap-container AREA").click(function(){
				jQuery('#cin-heatmap-container img.chr').removeClass('selected').css('display', 'none');
				jQuery('#cin-cytobands-container ul').removeClass('selected').css('display', 'none');

				var chrMap = '.'+$(this).attr('id')+'-map';
				var chrList = '.'+$(this).attr('id')+'-list';
				jQuery(chrMap).addClass('selected').css('display', 'inline');
				jQuery(chrList).addClass('selected').css('display', 'inline');
			});

		});

		function hovers(isover, y,x,w,h){
			if(isover){
				// showing div in position
				$("#cin-chr-selector").css({top:x+'px', left:y+'px', width:w+'px',height:h+'px',opacity:1}).show();
			} else {
				// hiding this div.
				$("#cin-chr-selector").hide();
			}
		}
		</g:javascript>
				
    </head>
    <body>
	<br/>
	<p style="font-size:14pt">CIN Results</p>
	<div id="centerContent" height="800px">
		<p style="font-size:12pt">Current Study: 
		<span id="label" style="display:inline-table">
			<g:if test="${!session.study}">no study currently selected</g:if>
			${session.study?.shortName}
		</span>
		</p>
		<br/>
		
		<g:render template="/cin/analysis_details" bean="${session.analysis}" />
		
		<br/>
		<br/>
	
		<g:render template="/cin/cinViewer"/>
		
	</div>
	<br/>
	<br/>
	</body>
	
</hmtl>