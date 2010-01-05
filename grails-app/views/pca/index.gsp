<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main" />
	<title>Analysis</title>         
</head>
<body>
	<jq:plugin name="flydisk" />
	<g:javascript>
	$(document).ready(function() {
		if($("input[name='reporterCriteria']:checked").size() < 1) {
			$("input[name='reporterCriteria']")[0].checked = true;
		}
		setupBoxes("input[name='reporterCriteria']");
		$("input[name='reporterCriteria']").change(function() {

			setupBoxes("input[name='reporterCriteria']");
		});
		jQuery().flydisk({ selectedColor:"#eee",                       //BgColor of selected items(Default: white) 
		left_disk:'left',                 //Id of left drop down list (Mandatory)
		right_disk:'right',               //Id of right drop down list(Mandatory)
		add_button: 'Add',                //Id of Add button            ,, 
		remove_button: 'Remove'

	});  
	$('#analysisForm').submit(function() {
		$('#right :option').attr("selected", "selected");
	});
	$('#right :option').each(function() {
		$("#left option[value='"+ this.value +  "']").remove();
	});
	$.sortOptions('#left');
});
	function setupBoxes(selector) {
		$(selector).each(function() {
			if(this.checked) {
				$(this).next('div').children('.validationInput').show('fast');
			} else {
				$(this).next('div').children('.validationInput').hide('fast');
			}
		});
	}

</g:javascript>
<p style="font-size:14pt">Perform Principal Component Analysis</p>
<br/>
<g:form name="analysisForm" action="submit">
<div class="clinicalSearch">
	<b>Patient Criteria</b>
	<br/>
	<br/>
	<g:radio name="patientCriteria" checked="true"/> All Patients <br/>
	<g:radio name="patientCriteria" disabled="true"/> Select Groups:
	<div id="patientListCriteria" style="display:none;">
	<br/>
	<table width="400px;">
		<tr>
			<td>
				<g:multiselect id="left" from="${session.patientLists}" optionKey="name" optionValue="name" 
						multiple="true" size="10" style="width: 150px"/>
			</td>
			<td>
				<table>
					<tr>
						<td>
							<a href="#" id="Add"> Add   </a> 
						</td>
					</tr>
					<tr>
						<td>
							<a href="#" id="Remove"> Remove   </a> 
						</td>
					</tr>
				</table>
			</td>
			<td>
				<g:multiselect id="right" name="groups" multiple="true" size="10" style="width: 150px"
					from="${flash.cmd?.groups}" class="${hasErrors(bean:flash.cmd,field:'groups','errors')}"/> 
			</td>
		</tr>
		<tr>
			<td colspan="3">
				&nbsp;
			</td>
		</tr>		
		<tr>
			<td colspan="3">
				<div class="errorDetail">
					<g:renderErrors bean="${flash.cmd?.errors}" field="groups" />
				</div>
			</td>
		</tr>
	</table>
	</div>
</div>
<div class="clinicalSearch">
	<b>Reporter Criteria</b>
	<br/>
	<br/>
	<%--
	<g:radio name="reporterCriteria" value="foldChange"/> Constrain reporters by fold change:
	<div id="reporterFold"  >	
	<g:validationInput name="foldChange"/> 
	</div>
	<br/>
	
	<g:radio name="reporterCriteria" value="variance" checked="${flash.cmd?.reporterCriteria == 'variance'}"/> 
	--%>
	<g:hiddenField name="reporterCriteria" value="variance"/>
	Constrain reporters by variance (Gene Vector) percentile:  ≥	
	<div id="reporterVariance" >
		<g:validationInput name="variance"/> 
	</div>
	<br/>
</div>
<div class="clinicalSearch">
		<%--
	Use Gene List:
	<br/>
	<g:select name="geneList" 
			noSelection="${['':'Select One...']}"
			from="${['T-Test: Two Sample Test', 'Wilcoxin Test: Mann-Whitney Test', 'F-Test: One Way ANOVA']}" />
	<br/>
	<br/>
	--%>
	Use Reporter List:
	<br/>
	<g:select name="reporterList"
				noSelection="${['':'Select One...']}"
				  from="${session.reporterLists}"
				optionKey="name" optionValue="name" />
	<br/>
	<br/>

	Dataset:
	<br/>
	<g:select name="dataFile" 
			from="${session.files}"
			optionKey="name" optionValue="${{it.description}}"/>
	<br/>
	<g:hiddenField name="study" value="${session.study.schemaName}" />
</div>
<br/>
<g:submitButton name="submit" value="Submit Analysis"/>
</g:form>
<br/>
<br/>

</body>

</hmtl>