<g:javascript library="jquery"/>

<div id="form">
		<div class="clinicalSearch">	
			<g:form name="reporterForm" action="submitGEPlot" url="${[action:'submitGEPlot']}">
			<table border="1">
			<tr><td colspan="3">
					<g:select name="groups"
							  noSelection="['':'-Choose list-']"
							  from="${session.lists}"
							optionKey="name" optionValue="name"
							  />
			</tr></td>
			<tr><td>
			Select Gene:
			</td><td>
			<g:validationInput name="geneName"/>
			</td></tr>
			<tr><td>
			<g:submitButton name="search" value="Plot"/>
			</td></tr>
			</table>
		</g:form>
		</div>
</div>

