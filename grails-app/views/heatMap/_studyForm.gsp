<g:if test="${session.study}">
<g:javascript src="dataSet.js"/>
<jq:plugin name="flydisk" />
<g:javascript>
$(document).ready( function () {
	$('input[type=submit]', this).attr('disabled', false);
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
	$('.patientRadio').change(function() {
		showGroups();
	});
	$('#right :option').each(function() {
			$("#left option[value='"+ this.value +  "']").remove();
	});
	$.sortOptions('#left');
	showGroups();
	 	$('#reporterForm').submit(function() {
			$('#search').attr("disabled", "true");
			$('#right :option').attr("selected", "selected");
 		});
	});

	function showGroups() {
		var selected = $("#patientList:checked").val();
		if(selected == 'ALL') {
			$('#patientListCriteria').fadeOut();
		} else {
			$('#patientListCriteria').fadeIn();
		}
	}
</g:javascript>
<g:if test="${(session.study.hasGenomicData() && session.dataSetType.contains('GENE EXPRESSION')) || (session.study.hasMicroRNAData() && session.dataSetType.contains('microRNA')) || (session.study.hasCopyNumberData() && session.dataSetType.contains('COPY_NUMBER')) || (session.study.hasMetabolomicsData() && session.dataSetType.contains('METABOLOMICS'))}">
<div id="searchForm">
		<div class="clinicalSearch">	
			<g:form name="reporterForm" action="drawHeatMap">
			<table class="formTable" cellpadding="2" cellspacing="2" style="border: none;">
			<tr>
				
				<td>
					Patient Criteria
				</td>
				<td>
					<g:radio name="patientList" class="patientRadio" checked="${!flash.cmd || (flash.cmd?.patientList == 'ALL')}" value="ALL"/> All Patients <br/>
					<g:radio name="patientList" class="patientRadio" checked="${flash.cmd?.patientList == 'GROUPS'}" value="GROUPS" /> Select Groups:
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
				</td>
			</tr>
			<tr>
				<td>
					Select Gene List:</td>
				<td colspan="2">

					<g:select name="geneList"
								  from="${session.geneLists}"
								noSelection="${['':'-Select a Gene List']}"
								optionKey="name" optionValue="name" />
					<img class="info" title="Please select a gene list ONLY if the data-type is GENE EXPRESSION. For all other data-types use an appropriate reporter list." src="${createLinkTo(dir:'images',file:'information.png')}" border="0" />
					<div class="errorDetail">
						<g:renderErrors bean="${flash.cmd?.errors}" field="geneList" />
					</div>								
				</td>
			</tr>
			<tr>
				<td>
				-OR-	Select Reporter List:</td>
				<td colspan="2">

					<g:select name="reporterList"
								  from="${session.reporterLists}"
								noSelection="${['':'-Select a Reporter List']}"
								optionKey="name" optionValue="name" />
								<img class="info" title="Please select an appropriate list for the data-type and dataset of interest: gene reporters for GENE EXPRESSION, miRNA reporters for microRNA, peaks for METABOLOMICS, cytobands for COPY_NUMBER - cytoband-level CIN and, chromosomes for COPY_NUMBER - chromosome-level CIN" src="${createLinkTo(dir:'images',file:'information.png')}" border="0" />
					<div class="errorDetail">
						<g:renderErrors bean="${flash.cmd?.errors}" field="reporterList" />
					</div>
				</td>
			</tr>
			<tr>
				<td>
					Data-Type
				</td>
				<td>
					<g:select name="dataSetType" 
							noSelection="${['':'Select Data Type']}"
							from="${session.dataSetType}"/>
				</td>
			</tr>	
			<tr>
				<td>
					Dataset:
				</td>
				<td>
					<div id="dataDiv">
					<g:select name="dataFile" 
							noSelection="${['':'Select Data Type First']}"
							optionKey="name" optionValue="${{it.description}}"/>
					</div>
					<div class="errorDetail">
						<g:renderErrors bean="${flash.cmd?.errors}" field="dataFile" />
					</div>
				</td>
			</tr>
			<tr>
				<td style="align:right" colspan="2">
					<g:submitButton name="search" value="Submit" />
				 <input type="reset" name="reset" value="reset" />
				</td>
				<td>
			</td>
			</tr>
			</table>
			<g:hiddenField name="study" value="${session.study.schemaName}" />
			<g:hiddenField name="fromComparison" value="false" />
			
		</g:form>
		</div>
</div>
</g:if>
<g:else>
	No expression, miRNA, copy number or metabolomics data available for ${session.study.shortName}
</g:else>
</g:if>