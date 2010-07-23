<g:if test="${session.study}">
<g:javascript src="dataSet.js"/>

<div id="searchForm">
		<div class="clinicalSearch">	
			<g:form name="reporterForm" action="drawHeatMap">
			<table class="formTable" cellpadding="2" cellspacing="2" style="border: none;">
			<tr>
				
				<td>Select a Patient Group</td>
				<td colspan="2">

					<g:select name="patientList"
							  from="${session.patientLists}"
							noSelection="${['ALL':'All Patients']}"
							optionKey="name" optionValue="name" />
					<div class="errorDetail">
						<g:renderErrors bean="${flash.cmd?.errors}" field="patientList" />
					</div>
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