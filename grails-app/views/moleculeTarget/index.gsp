<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />

	<g:javascript library="jquery"/>
	<script type="text/javascript" src="applets/jmol/Jmol.js"></script>
	<script type="text/javascript" src="applets/marvin/marvin.js"></script>
	<jq:plugin name="ui"/>
        <title>Molecule Target</title>         
    </head>
    <body>
	<p style="font-size:14pt">Molecule-Target Viewer (*beta*)</p>
	
		<g:if test="${bindings}">
	
		<g:each in="${bindings}" var="x">
		<g:each in="${x.structures}" var="structure">
		<g:set var="moleculeTargetPath" value="${grailsApplication.config.structuresPath + structure.structureFile.relativePath}" />
		<table class="viewerTable">
		<tr>
			<td><b>Protein</b>: ${x.protein.name}</td>
			
			<td valign="top" rowspan="5">
				<g:javascript>
					  jmolInitialize("applets/jmol","JmolAppletSigned0.jar");
				      jmolApplet([400,400], "load ${moleculeTargetPath};spacefill 0; wireframe 0.01;cartoon;color cartoon chain;select ligand;color yellow;");
				</g:javascript>
				<br />
				<b>View Options</b><br />
					<g:form>
					<g:radio value="view" checked="true"/> Protein-Ligand Complex<br />
					<g:radio value="view" /> Binding Pocket-Ligand Complex<br />
					<g:radio value="view" /> Protein Only<br />
					<g:radio value="view" /> Binding Pocket Only<br />
					</g:form>
			</td>
		</tr>
		<g:each in="${x.molecule.structures}" var="molStructure">
		<tr>
			<td><b>Source</b>: .pdb</td>
		</tr>
		<tr>
			<td><b>Ligand</b>: ${x.molecule.name}</td>
		</tr>
		<tr>
			<td>
				<g:set var="moleculePath" value="${grailsApplication.config.structuresPath + molStructure.structureFile.relativePath}" />
				<g:javascript>
				// marvin_jvm = "builtin"; // "builtin" or "plugin"
				mview_begin("applets/marvin/", 200, 200); //arguments: codebase, width, height
				// you could also use the mview_begin("../../..", 200, 200, true ); function call to load the applet without splash screen.
				mview_param("mol", "${moleculePath}");
				mview_param("background", "#ffffff");
				mview_param("molbg", "#ffffff");
				//mview_param("navmode", "rot3d");
				mview_param("rendering", "wireframe");
				//mview_param("animFPS", "20");
				mview_end();
				</g:javascript><br />
			</td>
		</tr>
		<tr>
			<td><b>Physico-Chemical Properties</b>: <br />
				<table style="font-size:.8em">
					<tr>
						<th>Property</th>
						<th>Value</th>
					</tr>
					<g:if test="${x.molecule.formula}">
					<tr>
						<td>Formula</td>
						<td>${x.molecule.formula}</td>
					</tr>
					</g:if>
					<g:if test="${x.molecule.weight}">
					<tr>
						<td>Molecular Weight</td>
						<td>${x.molecule.weight}</td>
					</tr>
					</g:if>
					<g:if test="${x.molecule.refractivity}">
					<tr>
						<td>Refractivity</td>
						<td>${x.molecule.refractivity}</td>
					</tr>
					</g:if>
					<g:if test="${x.molecule.solubility}">
					<tr>
						<td>Solubility</td>
						<td>${x.molecule.solubility}</td>
					</tr>
					</g:if>
					<g:if test="${x.molecule.ph}">
					<tr>
						<td>PH</td>
						<td>${x.molecule.ph}</td>
					</tr>
					</g:if>
					<g:if test="${x.molecule.donorAtoms}">
					<tr>
						<td>Donor Atoms</td>
						<td>${x.molecule.donorAtoms}</td>
					</tr>
					</g:if>
					<g:if test="${x.molecule.acceptorAtoms}">
					<tr>
						<td>Acceptor Atoms</td>
						<td>${x.molecule.acceptorAtoms}</td>
					</tr>
					</g:if>
					<g:if test="${x.molecule.clogP}">
					<tr>
						<td>ClogP</td>
						<td>${x.molecule.clogP}</td>
					</tr>
					</g:if>
					<g:if test="${x.molecule.rotatableBonds}">
					<tr>
						<td>Rotatable Bonds</td>
						<td>${x.molecule.rotatableBonds}</td>
					</tr>
					</g:if>
					<g:if test="${x.molecule.chiral}">
					<tr>
						<td>Chiral</td>
						<td>${x.molecule.chiral}</td>
					</tr>
					</g:if>
				</table>
		</td>
		</tr>
		
		</g:each>
		
		</table>
		</g:each>
		</g:each>
		</g:if>
		
		
		
		</body>
		
</html>