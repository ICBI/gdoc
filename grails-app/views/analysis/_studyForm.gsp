<g:if test="${session.study}">
<g:javascript src="dataSet.js"/>

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
	p-value:
	<br/>
	<g:validationInput name="pvalue" value=".05"/>
	<br/>
	Fold Change:
	<br/>
	<g:validationInput name="foldChange" value="2"/>
	<br/>
	Statistical Method:
	<br/>
	<g:select name="statisticalMethod" 
			from="${session.statisticalMethods}"
			value="'TTest'" optionKey="key" optionValue="value"/>
	<br/>
	<br/>
	Multiple Comparison Adjustment:
	<br/>
	<g:select name="adjustment" 
			from="${session.adjustments}"
			value="'NONE'" optionKey="key" optionValue="value"/>
	<br/>
	<br/>
	Data-Type<br />
	<g:select name="dataSetType" 
			noSelection="${['':'Select Data Type']}"
			from="${session.dataSetType}"/>
	<br/><br />
	Dataset:
	<br/>
	<div id="dataDiv">
	<g:select name="dataFile" 
			noSelection="${['':'Select Data Type First']}"
			optionKey="name" optionValue="${{it.description}}"/>
	</div>
	<div class="errorDetail">
		<g:renderErrors bean="${flash.cmd?.errors}" field="dataFile" />
	</div>
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
