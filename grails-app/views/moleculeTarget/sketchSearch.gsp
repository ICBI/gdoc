<html>
    <head>
        <meta name="layout" content="molSketch" />
		
	
	<g:javascript library="jquery"/>
	
	</head>
    <body>
		<g:javascript>

		function exportMol() {
			var ffmt = "smiles:";
			var s = document.Editor.getSmiles();
			//document.Editor.getMolFile();//document.MSketch.getMol(ffmt);
			//s = unix2local(s);
			if(s != "") {
				document.MolForm.smiles.value = s; 
				document.MolForm.submit();
			} else {
				alert("The drawing pallet is currently empty:\n"+
					"Click on the drawing pallet to add a molecular structure.\n");
				return false;
			}

		}
		</g:javascript>
<div >
	<p style="font-size:14pt">Molecule 'Sketch' Search | <g:link action="index">Input Search</g:link></p><br />
	<span class="applet">
	<applet code="org.openscience.jchempaint.applet.JChemPaintEditorApplet" archive="/gdoc/applets/jchemPaint/jchempaint-applet-core.jar"
			id="Editor"
	        width="500" height="400">
	<!--param name="load" value="applettests/big.mol"-->
	<param name="impliciths" value="true">
	<param name="codebase_lookup" value="false" />

	<PARAM name="onLoadTarget" value="statusFrame">
	<PARAM NAME="image" VALUE="hourglass.gif">
	<PARAM NAME="boxborder" VALUE="false">
	<PARAM NAME="centerimage" VALUE="true">
	</applet>
	<g:form name="MolForm" url='[controller: "moleculeTarget", action: "searchLigandsFromSketch"]'>
	<input value="Write SMILES String" onclick="exportMol()" type="button" style="display:none">
	<g:textField name="smiles" style="display:none"/>
	<br />
	<g:submitButton name="search_molecules" value="search molecules" onclick="exportMol();return false;"/>
	
	<input type="button" onclick="javascript:alert(document.Editor.getSmiles())" value="show smiles" />
	<input type="button" onclick="javascript:document.Editor.clear()" value="clear sketch area" />
	
	</g:form><br />
	
	
	</span>
	
	
	<g:if test="${ligands}">
		<span>Results for SMILES format, ${params.smiles}</span> 
		<div class="title">
		<div id="pager1" style="text-align:right;padding:2px 10px 3px 0px">
		<g:set var="totalPages" value="${Math.ceil(count / ligands.size())}" />

	    <g:if test="${totalPages == 1}">
	        <span class="currentStep">1</span>
	    </g:if>
	   	<g:else>
			<g:paginate controller="moleculeTarget" action="searchLigandsFromSketch" 
	        total="${count}" prev="&lt; previous" next="next &gt;" params="${['smiles':params.smiles,'offset':params.offset]}"/>
		</g:else>
		</div>
		</div>

		<table class="resultTable">
		<g:each in="${ligands}" var="molecule">
		<g:if test="${molecule}">
		<g:set var="moleculePaths" value="${molecule.structures?.toArray().collect{it.structureFile.relativePath}}" />

			<g:set var="ligandImg" value="${moleculePaths.find{it.contains('.png')}}" />
		<tr>
			<td style="border-bottom:1px solid orange;border-top:1px solid orange;background-color:#EBF1FF" colspan="3"><div style="width:400px;font-size:1em;border:0px solid black;text-wrap:suppress"><strong><u>NAME</u>: ${molecule.name}</strong></div></td></tr>
		<g:if test="${molecule.protectionGroup}">
			<tr><td colspan="3"><div style="font-size:.9em;">This compound is accessible to ${molecule.protectionGroup.name}</div></td></tr>
		</g:if>	
		<tr>

			<td valign="top">
				<table class="studyTable">
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
			<td valign="top">
				<table class="studyTable">
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
			</td>
			<td style="text-align:right">
				<img src="${grailsApplication.config.molecule2DstructuresPath}/${ligandImg}" />
				</td>

		</tr>
		<tr><td colspan="2">
			<div style="float:left;padding:10px;width:400px;text-wrap:suppress;">
				<img src="${createLinkTo(dir:'images',file:'target.png')}" border="0" />
				Targets:
				<g:if test="${molecule.bindings}">
					<g:each in="${molecule.bindings}" var="target">
						<g:link action="show" id="${target.id}">
							<g:set var="targetProt" value="${target.protein.gene?.geneAliases?.toArray().collect{it.symbol}}" />
								${targetProt.join(",")}
						</g:link>
					</g:each>
				</g:if>
				<g:else>
					No targets currently found for this compound
				</g:else>
				<br />
				
			</div>
			</td>
		</tr>
		</g:if>
		</g:each>
		</table>
		<div id="pager2" style="text-align:right;padding:2px 10px 3px 0px">
		<g:set var="totalPages" value="${Math.ceil(count / ligands.size())}" />

	    <g:if test="${totalPages == 1}">
	        <span class="currentStep">1</span>
	    </g:if>
	   	<g:else>
			<g:paginate controller="moleculeTarget" action="searchLigandsFromSketch" 
	        total="${count}" prev="&lt; previous" next="next &gt;" params="${['smiles':params.smiles,'offset':params.offset]}"/>
		</g:else>
		</div>
	</g:if> 
	<g:if test="${!ligands && search}">
		No compounds found for this sketch search, ${params.smiles}
	</g:if>
	
</div>

</body>
</html>