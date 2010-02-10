<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />

	<g:javascript library="jquery"/>
	<jq:plugin name="ui"/>
        <title>Molecule Target - Search</title>         
    </head>
    <body>
	<p style="font-size:14pt">Search for Targets (*beta*)</p><br />
<div id="centerContent">
<div class="clinicalSearch" style="width:61%">


<g:form url='[controller: "moleculeTarget", action: "searchLigands"]' id="searchableForm" name="searchableForm" method="get">
    <table class="formTable">
	<tr><td>Enter name for a gene, protein or molecule:	</td><td><g:textField name="entity" value="${params.entity}" size="10"/> </td></tr>
	<tr><td>Ligand Affinity: </td><td><g:textField disabled="true" name="ligandAffinity" value="${params.ligandAffinity}" size="10"/> </td></tr>
	<tr><td colspan="2">
		<div class="errorDetail">
			<g:renderErrors bean="${flash.cmd?.errors}" field="molWeightLow" />
		</div>
		<g:textField name="molWeightLow" value="${params.molWeightLow}" size="6"/>&nbsp;&lt;&nbsp;molecular weight&nbsp;&lt;&nbsp;<g:textField name="molWeightHigh" value="${params.molWeightHigh}" size="6"/></td></tr>
	<tr><td colspan="2" style="text-align:right"><input type="reset" value="Reset" /> | <input type="submit" value="Search" />
	</table>
	
</g:form>
</div>

</div>
<span>
    <g:if test="${ligands?.results}"><br />
     <span style="padding:15px"> Showing <strong>${ligands.offset + 1}</strong> - <strong>${ligands.results.size() + ligands.offset}</strong> of <strong>${ligands.total}</strong></span>
    </g:if>
    <g:else>
    &nbsp;
    </g:else>
  </span>
<div style="width:100%">
<g:if test="${ligands?.results}">
	<table class="resultTable" style="width:100%">
	<g:each in="${ligands.results}" var="molecule">
	<tr><td style="border-bottom:1px solid orange"><div style="font-size:1em;width:100%"><strong>${molecule.name}</strong></div></td></tr>
	<tr>
		<g:set var="ligandImg" value="${molecule.name + '.png'}" />
		<td valign="top" style="width:40%;padding-left:20px">
			<table class="studyTable" style="width:100%">
				<tr>
					<th>Property</th>
					<th>Value</th>
				</tr>
				<tr>
					<td>Formula</td>
					<td>${molecule.formula}</td>
				</tr>
				<tr>
					<td>Molecular Weight</td>
					<td>${molecule.weight}</td>
				</tr>
				<tr>
					<td>Refractivity</td>
					<td>${molecule.refractivity}</td>
				</tr>
				<tr>
					<td>Solubility</td>
					<td>${molecule.solubility}</td>
				</tr>
				<tr>
					<td>PH</td>
					<td>${molecule.ph}</td>
				</tr>
			</table>
		</td>
		<td valign="top" style="width:40%;padding-left:20px">
			<table class="studyTable" style="width:100%">
				<tr>
						<th>Property</th>
						<th>Value</th>
				</tr>
				<tr>
					<td>Donor Atoms</td>
					<td>${molecule.donorAtoms}</td>
				</tr>
				<tr>
					<td>Acceptor Atoms</td>
					<td>${molecule.acceptorAtoms}</td>
				</tr>
				<tr>
					<td>ClogP</td>
					<td>${molecule.clogP}</td>
				</tr>
				<tr>
					<td>Rotatable Bonds</td>
					<td>${molecule.rotatableBonds}</td>
				</tr>
				<tr>
					<td>Chiral</td>
					<td>${molecule.chiral}</td>
				</tr>
			</table><br />
			<div style="float:left;border:1px solid black;padding:10px">Targets:
				<g:each in="${molecule.bindings}" var="target">
					<g:link action="show" id="${target.id}">${target.protein.name}</g:link>
				</g:each>
			</div>
		</td>
		<td style="text-align:right"><img src="${createLinkTo(dir:'images/molecules',file:ligandImg)}" border="0" /></td>
		
	</tr>
	
	</g:each>
	</table>
</g:if> 
<div>
    <div class="paging">
      <g:if test="${ligands?.results}">
          Page:
          <g:set var="totalPages" value="${Math.ceil(ligands.total / ligands.max)}" />
          <g:if test="${totalPages == 1}"><span class="currentStep">1</span></g:if>
          <g:else><g:paginate controller="moleculeTarget" action="index" total="${ligands.total}" prev="&lt; previous" next="next &gt;"/></g:else>
      </g:if>
    </div>
  </div>
</div>
