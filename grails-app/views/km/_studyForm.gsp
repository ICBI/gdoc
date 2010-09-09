<jq:plugin name="flydisk" />
<jq:plugin name="ui"/>
<g:javascript src="dataSet.js"/>
<script type="text/javascript">
var geneExpression = false;
$(document).ready(function(){
  //$("#centerTabs").tabs();
	$('#centerTabs').tabs({ selected: 0 });
});
</script>
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

$('#reporterForm').submit(function() {
	$('#searchGE').attr("disabled", "true");
});
$('#searchForm').submit(function() {
	$('#right :option').attr("selected", "selected");
});
$('#right :option').each(function() {
	$("#left option[value='"+ this.value +  "']").remove();
});
$.sortOptions('#left');

if(geneExpression) {
	$('#centerTabs').tabs('select', 1);
}
});

</g:javascript>

<g:if test="${session.study}">
<g:if test="${session.endpoints}">
<div id="centerContent">
	<br/>
	<g:if test="${flash.cmd instanceof KmGeneExpCommand}">
		<g:javascript>
			geneExpression = true;
		</g:javascript>
	</g:if>
	<div class="tabDiv" style="border:0px solid black">
		
		<div id="centerTabs" class="tabDiv">
		    <ul>
		        <li><a href="#fragment-4"><span>Clinical KM</span></a></li>
						<g:if test="${session.study.hasGenomicData()}">
		        	<li><a href="#fragment-5"><span>Gene Expression KM</span></a></li>
						</g:if>
		    </ul>
			
			
		    <div id="fragment-4">
		        <g:form name="searchForm" action="search">
						
							<br/>
							Select Patient Groups:
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
						
								Endpoint:	
						
							<br/>
							<g:if test="${flash.cmd instanceof KmCommand}">
								<g:select name="endpoint" value="${flash.cmd?.endpoint}"
										noSelection="${['':'Select One...']}"
										from="${session.endpoints}" optionKey="attribute" optionValue="attributeDescription" class="${hasErrors(bean:flash.cmd,field:'endpoint','errors')}">
								</g:select>
							</g:if>
							<g:else>
								<g:select name="endpoint" style="height:20px"
										noSelection="${['':'Select One...']}"
										from="${session.endpoints}" optionKey="attribute" optionValue="attributeDescription" >
								</g:select>					
							</g:else>
								
								<div class="errorDetail">
									<g:if test="${flash.cmd instanceof KmCommand}">
										<g:renderErrors bean="${flash.cmd?.errors}" field="endpoint" />
									</g:if>
								</div>
				
					<br/>
					<g:hiddenField name="study" value="${session.study.schemaName}" />
					<br/>
					<g:submitButton name="search" value="Plot"/>
				</g:form>
		    </div>
				
		    	<div id="fragment-5">
						<g:if test="${session.study.hasGenomicData() && session.dataSetType.contains('GENE EXPRESSION')}">
						<g:javascript>
						function showGESpinner() {
							document.getElementById("GEspinner").style.visibility="visible" 
						}
						</g:javascript>
					
						<div id="form">
									
									<g:form name="reporterForm" action="submitGEPlot" url="${[action:'submitGEPlot']}">
									<table class="formTable" cellpadding="2" cellspacing="2" style="border: none;">
									<tr>
										<td>Select a Patient Group</td>
										<td colspan="2"><g:select name="groups"
													  from="${session.patientLists}"
													noSelection="${['ALL':'All Patients']}"
													optionKey="name" optionValue="name" /></td>
									</tr>
									<tr>
										<td>
											Select Gene:</td>
										<td colspan="2">
											<div class="validationInput">
												<g:if test="${flash.cmd instanceof KmGeneExpCommand}">
													<g:textField name="geneName" value="${flash.cmd?.geneName}"  class="${hasErrors(bean:flash.cmd,field:'geneName','errors')}"/>
												</g:if>
												<g:else>
													<g:textField name="geneName" />
												</g:else>
												<br/>
												<div class="errorDetail">
													<g:renderErrors bean="${flash.cmd?.errors}" field="geneName" />
												</div>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											Select Endpoint:</td>
										<td colspan="2">
											<g:if test="${flash.cmd instanceof KmGeneExpCommand}">
												<g:select name="endpoint" value="${flash.cmd?.endpoint}"
														noSelection="${['':'Select One...']}"
														from="${session.endpoints}" optionKey="attribute" optionValue="attributeDescription" class="${hasErrors(bean:flash.cmd,field:'endpoint','errors')}">
												</g:select>
											</g:if>
											<g:else>
												<g:select name="endpoint" 
														noSelection="${['':'Select One...']}"
														from="${session.endpoints}" optionKey="attribute" optionValue="attributeDescription" >
												</g:select>					
											</g:else>
											<br/>
											<div class="errorDetail">
												<g:if test="${flash.cmd instanceof KmGeneExpCommand}">
													<g:renderErrors bean="${flash.cmd?.errors}" field="endpoint" />
												</g:if>
											</div>
											<g:hiddenField name="study" value="${session.study.schemaName}" />
										</td>
									</tr>
									<tr>
										<td>Select Datatype: is</td>
										<td colspan="2">
											<g:select name="dataSetType" 
													noSelection="${['':'Select Data Type']}"
													from="${session.dataSetType}"/>
										</td>
									</tr>
									<tr>
										<td>Select Dataset:</td>
										<td colspan="2">
											<div id="dataDiv">
											<g:select name="dataFile" 
													noSelection="${['':'Select Data Type First']}"
													optionKey="name" optionValue="${{it.description}}"/>
											</div>
											<div class="errorDetail">
												<g:renderErrors bean="${flash.cmd?.errors}" field="dataFile" />
											</div>	
										</td>
									</td>			
									<tr>
										<td style="align:right" colspan="2">
											<g:submitButton name="searchGE" value="Plot" onclick="showGESpinner();"/>
										 <input type="reset" name="reset" value="reset" />
										</td>
										<td>
										<span id="GEspinner" style="visibility:hidden"><img src='/gdoc/images/spinner.gif' alt='Wait'/>...performing 				            gene expression analysis</span>
									</td>
									</tr>
									</table>

								</g:form>
								</div>
								
								</g:if>
								<g:else>
									No expression data available for ${session.study.shortName}
								</g:else>
					
				</div>
					
		</div>		
		
				
	</div>

</div>

</g:if>
<g:else>
	No Kaplan-Meier endpoint data is available for ${session.study.shortName}
</g:else>
</g:if>
<g:else>
<p>No study currently selected.</p>
</g:else>