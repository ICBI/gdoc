<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />

	<g:javascript library="jquery"/>
	<script type="text/javascript" src="/gdoc/applets/jmol/Jmol.js"></script>
	<script type="text/javascript" src="/gdoc/applets/marvin/marvin.js"></script>
	<g:javascript>
	$(document).ready( function () {
				$('#moleculeSelector').change(function() {
					if($('#moleculeSelector').val()) {
						$('#selectorForm').submit();
					}
		 		});
			
	} );
	
		
	
	
	</g:javascript>
	<jq:plugin name="ui"/>
        <title>Molecule Target</title>         
    </head>
    <body>
		<%
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() +  request.getContextPath();
		%>
	<p style="font-size:14pt">Molecule-Target Viewer | <g:link action="index">target search</g:link>
		<span style="float:right;font-size:.7em;padding:5px">
			<g:form name="selectorForm" action="loadSimilar">
			Other Molecules for this target:&nbsp;
			<g:select name="moleculeSelector" 
				noSelection="${['':'Choose targeting-molecule...']}"
				from="${similarTargets}"
				optionKey="id">
			</g:select>
			</g:form>
		</span></p>
	
		<g:if test="${moleculeTarget}">
		<g:each in="${moleculeTarget.structures}" var="structure">
		<g:set var="moleculeTargetPath" value="${structure.structureFile.relativePath}" />
		<table class="viewerTable">
		<tr>
			<td><b>Protein</b>: ${moleculeTarget.protein.name}</td>
			
			<td valign="top" rowspan="5">
				<g:javascript>
					  jmolInitialize("/gdoc/applets/jmol","JmolAppletSigned0.jar");
				      jmolApplet([400,400], "load ${basePath}/moleculeTarget/display?inputFile=${moleculeTargetPath};spacefill 0; wireframe 0.01;cartoon;color cartoon chain;select ligand;color yellow;");
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
		<g:each in="${moleculeTarget.molecule.structures}" var="molStructure">
		<tr>
			<td><b>Source</b>: .pdb</td>
		</tr>
		<tr>
			<td><b>Ligand</b>: ${moleculeTarget.molecule.name}</td>
		</tr>
		<tr>
			<td>
				<g:set var="moleculePath" value="${molStructure.structureFile.relativePath}" />
				<g:javascript>
				// marvin_jvm = "builtin"; // "builtin" or "plugin"
				mview_begin("/gdoc/applets/marvin/", 200, 200); //arguments: codebase, width, height
				// you could also use the mview_begin("../../..", 200, 200, true ); function call to load the applet without splash screen.
				mview_param("mol", "/gdoc/moleculeTarget/display?inputFile=${moleculePath}");
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
					<g:if test="${moleculeTarget.molecule.formula}">
					<tr>
						<td>Formula</td>
						<td>${moleculeTarget.molecule.formula}</td>
					</tr>
					</g:if>
					<g:if test="${moleculeTarget.molecule.weight}">
					<tr>
						<td>Molecular Weight</td>
						<td>${moleculeTarget.molecule.weight}</td>
					</tr>
					</g:if>
					<g:if test="${moleculeTarget.molecule.refractivity}">
					<tr>
						<td>Refractivity</td>
						<td>${moleculeTarget.molecule.refractivity}</td>
					</tr>
					</g:if>
					<g:if test="${moleculeTarget.molecule.solubility}">
					<tr>
						<td>Solubility</td>
						<td>${moleculeTarget.molecule.solubility}</td>
					</tr>
					</g:if>
					<g:if test="${moleculeTarget.molecule.ph}">
					<tr>
						<td>PH</td>
						<td>${moleculeTarget.molecule.ph}</td>
					</tr>
					</g:if>
					<g:if test="${moleculeTarget.molecule.donorAtoms}">
					<tr>
						<td>Donor Atoms</td>
						<td>${moleculeTarget.molecule.donorAtoms}</td>
					</tr>
					</g:if>
					<g:if test="${moleculeTarget.molecule.acceptorAtoms}">
					<tr>
						<td>Acceptor Atoms</td>
						<td>${moleculeTarget.molecule.acceptorAtoms}</td>
					</tr>
					</g:if>
					<g:if test="${moleculeTarget.molecule.clogP}">
					<tr>
						<td>ClogP</td>
						<td>${moleculeTarget.molecule.clogP}</td>
					</tr>
					</g:if>
					<g:if test="${moleculeTarget.molecule.rotatableBonds}">
					<tr>
						<td>Rotatable Bonds</td>
						<td>${moleculeTarget.molecule.rotatableBonds}</td>
					</tr>
					</g:if>
					<g:if test="${moleculeTarget.molecule.chiral}">
					<tr>
						<td>Chiral</td>
						<td>${moleculeTarget.molecule.chiral}</td>
					</tr>
					</g:if>
				</table>
		</td>
		</tr>
		
		</g:each>
		
		</table>
		</g:each>
	
		</g:if>
		<g:else><br /><br />
		No bindings found
		</g:else>
		</body>
		
</html>