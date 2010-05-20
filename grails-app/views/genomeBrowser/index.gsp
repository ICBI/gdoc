<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main" />
	<g:javascript library="jquery" />
	<jq:plugin name="blockui" />
	<title>GDOC Genome Browser</title>         
</head>
<body>
<g:javascript>
	$(document).ready(function() {
		toggleBlock($('#omics'));
		$('#omics').change(function() {
			toggleBlock(this);
		});
		$('#submit').click(function() {
			$('#browseForm').submit();
		})
		$('.searchRadio').change(function() {
			$('.search').toggle();
		})
	});
	
	function toggleBlock(item) {
		if($(item).attr("checked")) {
			$('#omicsDiv').unblock();
		} else {
			$('#omicsDiv').block({
				message: null
			});
		}
	}
</g:javascript>
<p style="font-size:14pt">Genome Browser</p>
<br/>
<div>
	<g:form name="browseForm" action="view">
		<div class="clinicalSearch">
			Select Location Criteria<br/><br/>
			<g:radio name="searchType" value="gene" checked="true" class="searchRadio"/> Browse to Gene <br/><br/>
			<g:radio name="searchType" value="location" class="searchRadio"/> Browse to Chromosome Location <br/><br/>
			<div id="geneSearch" class="search">
				Enter Gene Name: <g:textField name="gene"/><br/><br/>
			</div>
			<div id="locationSearch" class="search" style="display: none">
				Enter Chromosome: 
				<g:select name="chromosome" from="${session.chromosomes}">
				</g:select>
				Location: 
				<g:textField name="location"/><br/><br/>
			</div>
			<g:checkBox name="omicsData" id="omics"/> Add Omics Data to Display<br/><br/>
		</div>
	</g:form>
		
		<div class="clinicalSearch" id="omicsDiv">
			<div id="studyPicker">
				<g:render template="/studyDataSource/studyPicker"/>
			</div>
			<div id="searchDiv">
				<g:render template="studyForm"/>
			</div>
				
		</div>
		<br/>
		<g:submitButton name="submit" value="Submit"/>
</div>

</body>

</hmtl>