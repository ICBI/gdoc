<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main" />
	<g:javascript library="jquery" />
	<title>Analysis</title>         
</head>
<body>
	<jq:plugin name="flydisk" />
	<g:javascript>
	$(document).ready(function() {

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

</g:javascript>
<p style="font-size:14pt">Perform Class Comparison Analysis</p>

<div id="studyPicker">
	<g:render template="/studyDataSource/studyPicker"/>
</div>

<div id="searchDiv">
	<g:render template="/analysis/studyForm"/>
</div>

</body>

</hmtl>