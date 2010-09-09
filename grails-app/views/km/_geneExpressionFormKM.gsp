<g:javascript library="jquery"/>
<g:javascript>
$(document).ready( function () {
	 	$('#search').click(function() {
			$(this).attr("disabled", "true");
			showSpinner();
 		});
	});

</g:javascript>

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
								from="${endpoints}" optionKey="attribute" optionValue="attributeDescription" class="${hasErrors(bean:flash.cmd,field:'endpoint','errors')}">
						</g:select>
					</g:if>
					<g:else>
						<g:select name="endpoint" 
								noSelection="${['':'Select One...']}"
								from="${endpoints}" optionKey="attribute" optionValue="attributeDescription" >
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
				<td style="align:right" colspan="2">
					<g:submitButton name="search" value="Plot"/>
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

