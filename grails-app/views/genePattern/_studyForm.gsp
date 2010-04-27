<g:if test="${session.study}">
<g:javascript library="jquery"/>
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
<br>
<g:form name="genePatternForm" action="submit">
<div class="clinicalSearch">
	<br/>
	Select Groups:
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
	<br/>
	Gene List:
	<br/>
	<g:select name="geneList" 
			from="${session.geneLists}"
			noSelection="${['':'Select One...']}"
			optionKey="name" optionValue="name"/>
	<br/>
	<br/>
	Dataset:
	<br/>
	<g:select name="dataFile" 
			from="${session.files}"
			optionKey="name" optionValue="${{it.description}}"/>
	<br/>
	<br/>
	Analysis Name:
	<br/>
	<g:validationInput name="analysisName"/>
	<br/>
</div>
<br/>
<g:submitButton name="submit" value="Submit GenePattern Analysis"/>
</g:form>
</g:if>

<g:else>
<p>No study currently selected.</p>
</g:else>