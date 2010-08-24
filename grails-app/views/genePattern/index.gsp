<html>
<head>
	<meta name="layout" content="main" />
	<g:javascript library="jquery"/>
	<title>Submit GenePattern Analysis</title>         
</head>
<body>
	<jq:plugin name="flydisk" />
	<g:javascript>
		$(document).ready(function() {

				jQuery().flydisk({ selectedColor:"#eee",                       //BgColor of selected items(Default: white) 
				left_disk:'left',                 //Id of left drop down list (Mandatory)
				right_disk:'right',               //Id of right drop down list(Mandatory)
				add_button: 'Add',                //Id of Add button            ,, 
				remove_button: 'Remove',          //Id of Remove ,,           (Mandatory)  
				up_button : 'Up',                 //Id of Up     ,,           (Optional)
				down_button: 'Down',              //Id of Down   ,,    
				move_all_button :'move_all',      //Id of Move  all button        ,,     
				remove_all_button :'remove_all',  //Id of Remove  ,,              ,,
				move_top_button   : 'move_top',   //Id of Move top button         ,,
				move_bottom_button: 'move_bottom' //Id of Move bottom ,,          ,,  
			});  
			$('#genePatternForm').submit(function() {
				$('#right :option').attr("selected", "selected");
			});
			$('#right :option').each(function() {
				$("#left option[value='"+ this.value +  "']").remove();
			});
			$.sortOptions('#left');
		});

	</g:javascript>
	<p style="font-size:14pt">Submit GenePattern Analysis</p>
	<br/>
	<div id="studyPicker">
		<g:render template="/studyDataSource/studyPicker"/>
	</div>

	<div id="searchDiv">
		<g:render template="/genePattern/studyForm"/>
	</div>
	

</body>

</hmtl>