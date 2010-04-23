<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
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
				$("#disease").change(function() {
					jQuery.getJSON("/gdoc/studyDataSource/findStudiesForDisease", { disease: $("#disease").val() },
						function(j) {
						     // erase all OPTIONs from existing select menu on the page
						    $("#study options").remove();
 							// You will rebuild new options based on the JSON response...
						     var options = "<option value=''>Select Study...</option>";
						     // it is the array of key-value pairs to turn
						     // into option value/labels...
						     for (var i = 0; i < j.length; i++)
						       {
								//console.log("load " + j[i]);
						        options += "<option value=" +
						        j[i].studyId + ">" +
						        j[i].studyName +
						        "</option>";
						        }
						        // stick these new options in the existing select menu
						        $("#study").html(options);
						        // now your select menu is rebuilt with dynamic info
						  }
					); // end getJSON
		 		  }); // end clicked button to trigger AJAX
				$("#study").click(function() {
					$("#update").removeAttr('disabled');
				});
			});
		
		function reload(){
			$("#searchDiv").load('_studyForm.gsp').fadeIn("slow");
			$("#changeStudy").css("display","block");
		}
		
		function showStudyChange(){
			console.log($("#studyFieldset1"));
			$("#studyFieldset").css("display","block");
		}
		
		</g:javascript>
	<p style="font-size:14pt">Search Clinical Data</p>
	<div id="centerContent">
		<br/>
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
		
		
			<p>Current Study: 
				<span id="label"><g:if test="${!session.study}">no study selected</g:if>${session.study?.shortName}</span> 
				<g:if test="${session.study}">
					<a href="#" id="changeStudy" style="display:block" onclick="showStudyChange();">change study?</a>
				</g:if>
				<g:else>
					<a href="#" id="changeStudy" style="display:none" onclick="showStudyChange();">change study?</a>
				</g:else>
			</p>
		
		<g:if test="${!session.study}">
		<div id="studyFieldset">
		</g:if>
		<g:else>
		<div id="studyFieldset" style="display:none">
		</g:else>
		<fieldset style="border:1px solid blue;margin:10px;"><legend style="margin:8px">Choose a disease and study</legend>
			<g:formRemote name="setStudyForm" url="[controller:'studyDataSource',action:'setStudy']" update="label" onSuccess="reload();">
		Disease:
		<g:select name="disease" 
				noSelection="${['':'Select Disease...']}"
				from="${diseases}">
		</g:select>
		
		Study:
		<g:select name="study" 
				noSelection="${['':'Select Study...']}"
				from="${myStudies}"
				value="${session.study?.id}"
				optionKey="id" optionValue="shortName">
		</g:select>
		
		    <g:submitButton name="update" value="set study" disabled="true"/>
		</g:formRemote>
		</fieldset>
		</div>

		
		<div id="searchDiv">
			<g:render template="/clinical/studyForm"/>
		</div>
		
		
	</div>
	</body>
	
</hmtl>