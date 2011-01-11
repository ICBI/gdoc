
		<g:javascript library="jquery" />
		<jq:plugin name="tooltip"/>
		<jq:plugin name="ui"/>
		<g:javascript>
			$(document).ready(function (){
				$("#disease").change(function() {
					queryForDisease();
		 		  }); // end clicked button to trigger AJAX
				if($("#disease").val()) {
					queryForDisease();
				}
				$("#changeStudy").click(function() {
				  	showStudyChange();
				});
			});
		function queryForDisease() {
			jQuery.getJSON("/gdoc/studyDataSource/findStudiesForDisease", { disease: $("#disease").val() },
				function(j) {
				     // erase all OPTIONs from existing select menu on the page
				    $("#study options").remove();
					$("#study").bind('change', function() {
						$("#update").removeAttr('disabled');
					});
					// You will rebuild new options based on the JSON response...
				     var options = "<option value=''>Select A Study ...</option>";
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
		}
		function reload(){
			if(${remote ?: false}) {
				$("#searchDiv").load('/gdoc/${controllerName}/_studyForm.gsp',{limit: 25}, function(){
					if(typeof bindBehaviour == 'function')
						bindBehaviour();
					});
				$("#changeStudy").css("display","block");
				$("#studyPageSpinner").css("visibility","hidden");
			} else {
				//location.reload(true);
				location.replace('/gdoc/${controllerName}');
			}
			
		}
		
	
		function showStudyChange(){
			var visSelector = $("#studyFieldset").css("display");
			if(visSelector == 'none'){
				$("#studyFieldset").css("display","block");
				$("#changeStudy").html("hide study selector");
			}
			else{
				$("#studyFieldset").css("display","none");
				$("#changeStudy").html("change study?");	
			}
		}
		function showSpinner(){
			$("#studyPageSpinner").css("visibility","visible");
		}
		</g:javascript>
		<p style="font-size:12pt">Current Study: 
			<span id="label" style="display:inline-table">
				<g:if test="${!session.study}">no study currently selected</g:if>
				${session.study?.shortName}</span>
			<span style="display:inline-table;font-size:.8em">
				<g:if test="${session.study}">
					<a href="#" id="changeStudy" style="display:block;margin-left:26px">change study?</a>
				</g:if>
				<g:else>
					<a href="#" id="changeStudy" style="display:none;margin-left:26px">change study?</a>
				</g:else>
			</span>
		</p>
		<g:if test="${!session.study}">
		<div id="studyFieldset">
		</g:if>
		<g:else>
		<div id="studyFieldset" style="display:none">
		</g:else>
		<fieldset style="border:1px solid #336699;margin:10px;padding:8px;background-color:beige"><legend style="margin:8px">Choose a disease and study</legend>
			<g:formRemote name="setStudyForm" url="[controller:'studyDataSource',action:'setStudy']" update="label" onLoading="showSpinner();" onSuccess="reload();">
		Disease:
		<g:select name="disease" 
				noSelection="${['':'Select Disease...']}"
				from="${diseases}">
		</g:select>
		
		Study:
		<g:select name="study" 
				noSelection="${['':'First Select Disease...']}">
				
		</g:select>
		<%--from="${myStudies}"
		value="${session.study?.id}"
		optionKey="id" optionValue="shortName"--%>
			<br /><span style="float:right">
		    <g:submitButton name="update" value="set study" disabled="true"/>
			</span>
			<span id="studyPageSpinner" style="visibility:hidden;display:inline-table"><img src='/gdoc/images/spinner.gif' alt='Wait'/></span>
		</g:formRemote>
		
		</fieldset>
		</div>