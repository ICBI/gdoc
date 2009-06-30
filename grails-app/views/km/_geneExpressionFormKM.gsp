<g:javascript library="jquery"/>

<g:javascript>
function showSpinner() {
	document.getElementById("spinner").style.visibility="visible" 
}
</g:javascript>

<div id="form">
		<div class="clinicalSearch">	
			<g:form name="reporterForm" action="submitGEPlot" url="${[action:'submitGEPlot']}">
			<table cellpadding="2" cellspacing="2" border="1">
			<tr>
				<td>Select a Patient Group (optional)</td>
				<td><g:select name="groups"
							  noSelection="['':'-All patients-']"
							  from="${session.patientLists}"
							optionKey="name" optionValue="name" /></td>
			</tr>
			<tr>
				<td>
					Select Gene:</td>
				<td><g:validationInput name="geneName"/></td>
			</tr>
			<tr>
				<td>
					<g:submitButton name="search" value="Plot" onclick="showSpinner();"/></td>
				<td>
				<span id="spinner" style="visibility:hidden"><img src='/gdoc/images/spinner.gif' alt='Wait'/>...performing 				            gene expression analysis</span>
			</td>
			</tr>
			</table>
		</g:form>
		</div>
</div>

