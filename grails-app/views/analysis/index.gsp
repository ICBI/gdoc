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
});

</g:javascript>
<p style="font-size:14pt">Analysis</p>
<br/>
<g:form name="analysisForm" action="submit">
<div class="clinicalSearch">
	<div style="float: left">
		<b>Class Comparison</b>
	</div>
	<br/>
	<br/>
	Select Groups:
	<br/>
	<table>
		<tr>
			<td>
				<g:multiselect id="left" from="${session.lists}" optionKey="id" optionValue="name" 
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
				<g:multiselect id="right" name="groups" multiple="true" size="10" style="width: 150px"/> 
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	p-value:
	<br/>
	<g:textField name="pValue"  />
	<br/>
	<br/>
	Fold Change:
	<br/>
	<g:textField name="foldChange"  />
	<br/>
	<br/>
</div>
<br/>
<g:submitButton name="submit" value="Submit Analysis"/>
</g:form>


</body>

</hmtl>