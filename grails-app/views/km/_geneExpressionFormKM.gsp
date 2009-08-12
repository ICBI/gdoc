<g:javascript library="jquery"/>

<g:javascript>
function showSpinner() {
	document.getElementById("spinner").style.visibility="visible" 
}
</g:javascript>

<div id="form">
		<div class="clinicalSearch">	
			<g:form name="reporterForm" action="submitGEPlot" url="${[action:'submitGEPlot']}">
			<table class="formTable" cellpadding="2" cellspacing="2" style="border: none;">
			<tr>
				<td>Select a Patient Group</td>
				<td colspan="2"><g:select name="groups"
							  from="${session.patientLists}"
							optionKey="name" optionValue="name" /></td>
			</tr>
			<tr>
				<td>
					Select Gene:</td>
				<td colspan="2"><g:validationInput name="geneName"/></td>
			</tr>
			<tr>
				<td>
					Select Endpoint:</td>
				<td colspan="2">
					<g:select name="endpoint" 
							noSelection="${['':'Select One...']}"
							from="${endpoints}" optionKey="attribute" optionValue="attributeDescription">
					</g:select>
				</td>
			</tr>			
			<tr>
				<td style="align:right" colspan="2">
					<g:submitButton name="search" value="Plot" onclick="showSpinner();"/>
				 <input type="reset" name="reset" value="reset" />
				</td>
				<td>
				<span id="spinner" style="visibility:hidden"><img src='/gdoc/images/spinner.gif' alt='Wait'/>...performing 				            gene expression analysis</span>
			</td>
			</tr>
			</table>
		</g:form>
		</div>
</div>

