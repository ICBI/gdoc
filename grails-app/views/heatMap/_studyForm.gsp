<g:if test="${session.study}">

<g:javascript library="jquery"/>
<g:javascript>
$(document).ready(function() {

$("#dataSetType").change(function() {
	loadDataSets(this.value)
});

loadDataSets($('#dataSetType').val());

});

function loadDataSets(dataType) {
	$.ajax({
		url: "${createLink(action:'selectDataType')}",
		data: "dataType=" + dataType,
		cache: false,
		success: function(html) {
 			$("#dataDiv").html(html);
		}
	});
}
</g:javascript>
<div id="searchForm">
		<div class="clinicalSearch">	
			<g:form name="reporterForm" action="drawHeatMap">
			<table class="formTable" cellpadding="2" cellspacing="2" style="border: none;">
			<tr>
				
				<td>Select a Patient Group</td>
				<td colspan="2">
					<div class="errorDetail">
						<g:renderErrors bean="${flash.cmd?.errors}" field="patientList" />
					</div>
					<g:select name="patientList"
							  from="${session.patientLists}"
							noSelection="${['ALL':'All Patients']}"
							optionKey="name" optionValue="name" /></td>
			</tr>
			<tr>
				<td>
					Select Gene List:</td>
				<td colspan="2">
					<div class="errorDetail">
						<g:renderErrors bean="${flash.cmd?.errors}" field="geneList" />
					</div>
					<g:select name="geneList"
								  from="${session.geneLists}"
								noSelection="${['':'-Select a Gene List']}"
								optionKey="id" optionValue="name" />
				</td>
			</tr>
			<tr>
				<td>
				-OR-	Select Reporter List:</td>
				<td colspan="2">
					<div class="errorDetail">
						<g:renderErrors bean="${flash.cmd?.errors}" field="reporterList" />
					</div>
					<g:select name="reporterList"
								  from="${session.reporterLists}"
								noSelection="${['':'-Select a Reporter List']}"
								optionKey="name" optionValue="name" />
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
		</g:form>
		</div>
</div>

</g:if>