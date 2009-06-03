<g:javascript library="jquery"/>

<div id="form">
		<div class="clinicalSearch">	
		<g:formRemote name="reporterForm" action="findHighestMeanReporters" url="${[action:'findHighestMeanReporters']}">
			<table border="1">
			<tr><td colspan="3">
					<g:select name="groups"
							  noSelection="['':'-Choose lists-']"
							  from="${session.lists}"
							  multiple="true"
							  size="3"
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
		</g:formRemote>
		</div>
</div>

<div id="results">

</div>
