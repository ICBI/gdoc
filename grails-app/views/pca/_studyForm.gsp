<g:javascript src="dataSet.js"/>
<jq:plugin name="flydisk" />
<p>Select a patient list, optional reporter list, classification method, and datatype/dataset</p>

<g:if test="${session.study}">
<g:javascript>
	$(document).ready( function () {
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
	 	$('#analysisForm').submit(function() {
			$('#submit').attr("disabled", "true");
 		});
		$('.patientRadio').change(function() {
			showGroups();
		});
		showGroups();
	});
	function showGroups() {
		var selected = $("#patientCriteria:checked").val();
		if(selected == 'ALL') {
			$('#patientListCriteria').hide();
		} else {
			$('#patientListCriteria').show();
		}
	}

</g:javascript>
	
	<g:form name="analysisForm" action="submit">
	<div class="clinicalSearch">
		<b>Patient Criteria</b>
		<br/>
		<g:radio name="patientCriteria" class="patientRadio" checked="${!flash.cmd || (flash.cmd?.patientCriteria == 'ALL')}" value="ALL"/> All Patients <br/>
		<g:radio name="patientCriteria" class="patientRadio" checked="${flash.cmd?.patientCriteria == 'GROUPS'}" value="GROUPS" /> Select Groups:
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

		Use Reporter List:
		<br/>
		<g:select name="reporterList"
					noSelection="${['':'Select One...']}"
					  from="${session.reporterLists}"
					optionKey="name" optionValue="name" />
		<br/>
		<br/>

		Classification Method:
		<br/>
		<g:select name="classificationMethod" 
				from="${['PCA: Principal Component Analysis']}"
				value="'PCA: Principal Component Analysis'" />
		<br/>
		<br />
		Data-Type<br />
		<g:select name="dataSetType" 
				noSelection="${['':'Select Data Type']}"
				from="${session.dataSetType}"/>
		<br/><br />
		Dataset:
		<br/>
		<div id="dataDiv">
		<g:select name="dataFile" 
				noSelection="${['':'Select Data Type First']}"
				optionKey="name" optionValue="${{it.description}}"/>
		</div>
		<div class="errorDetail">
			<g:renderErrors bean="${flash.cmd?.errors}" field="dataFile" />
		</div>
		<g:hiddenField name="study" value="${session.study.schemaName}" />
	</div>
	<br/>
	<g:submitButton name="submit" value="Submit Analysis"/>
	</g:form>

</g:if>

<g:else>
<p>No study currently selected.</p>
</g:else>