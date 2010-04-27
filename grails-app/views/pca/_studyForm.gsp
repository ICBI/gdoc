<p>Select a patient list, optional reporter list, classification method, and datatype/dataset</p>

<g:if test="${session.study}">
	
	<g:form name="analysisForm" action="submit">
	<div class="clinicalSearch">
		<b>Patient Criteria</b>
		<br/>

		<g:radio name="patientCriteria" checked="true"/> All Patients <br/>
		<g:radio name="patientCriteria" disabled="true"/> Select Groups:
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
		Dataset:
		<br/>
		<g:select name="dataFile" 
				from="${session.files}"
				optionKey="name" optionValue="${{it.description}}"/>
		<br/>
		<g:hiddenField name="study" value="${session.study.schemaName}" />
	</div>
	<br/>
	<g:submitButton name="submit" value="Submit Analysis"/>
	</g:form>

</g:if>

<g:else>
<p>No study currently selected.</p>
</g:else>