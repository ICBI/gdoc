<g:javascript src="dataSet.js"/>

<g:if test="${session.study}">
<g:javascript>
$(document).ready( function () {
	 	$('#analysisForm').submit(function() {
			$('#submit').attr("disabled", "true");
 		});
	});

</g:javascript>
<g:if test="${session.study.hasCopyNumberData()}">
	<p>Select a baseline group and a comparison group(s)</p>
	<g:if test="${flash.message}">
	<div class="message" style="width:75%">
		${flash?.message}
	</div>
	</g:if>
	<g:form name="analysisForm" action="submit">

	<div class="clinicalSearch">
		<br />

		Select baseline group:
		<g:select name="baselineGroup"
				noSelection="${['':'Select base-line group...']}"
				value="${flash.cmd?.baselineGroup}"
				from="${session.patientLists}" optionKey="name" optionValue="name"	/>			
		<br/>
		<div class="errorDetail">
			<g:renderErrors bean="${flash.cmd?.errors}" field="baselineGroup" />
		</div>
		<br />
		Select comparison group:
		<g:select name="groups"
			noSelection="${['':'Select comparison group...']}"
			value="${flash.cmd?.groups}"
			from="${session.patientLists}" optionKey="name" optionValue="name"	/>
		<br/>
		<div class="errorDetail">
			<g:renderErrors bean="${flash.cmd?.errors}" field="groups" />
		</div>
		<br/>
		<g:hiddenField name="dataFile" value="${session.df}" />
		<g:hiddenField name="cytobandsDataFile" value="${session.cdf}" />
		<g:hiddenField name="study" value="${session.study.schemaName}" />
	</div>
	<br/>
	<g:submitButton name="submit" value="Submit Analysis"/>
	</g:form>
</g:if>
<g:else>
	No copy number data available for ${session.study.shortName}
</g:else>
</g:if>

<g:else>
<p>No study currently selected.</p>
</g:else>