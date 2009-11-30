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
	<p style="font-size:14pt">Molecule-Target Viewer</p>
		<table class="viewerTable">
		<tr>
			<td><b>Protein</b>: EGFR</td>
			
			<td rowspan="6">
				<g:javascript>
					  jmolInitialize("applets/jmol","JmolAppletSigned0.jar");
				      jmolApplet([320,400], "load ${moleculeTargetPath};select all;spacefill 0; wireframe 0.01;cartoon;color cartoon chain;");
				</g:javascript>
			</td>
		</tr>
		<tr>
			<td><b>Source</b>: PDB</td>
		</tr>
		<tr>
			<td><b>Ligand</b>: Ligand Name (CAS, IUPAC)	</td>
		</tr>
		<tr>
			<td><b>Physico-Chemical Properties</b>: MW, LogP, etc	</td>
		</tr>
		<tr>
			<td>
		<g:javascript>
		// marvin_jvm = "builtin"; // "builtin" or "plugin"
		mview_begin("applets/marvin/", 200, 200); //arguments: codebase, width, height
		// you could also use the mview_begin("../../..", 200, 200, true ); function call to load the applet without splash screen.
		mview_param("mol", "${moleculePath}");
		mview_param("background", "#ffffff");
		mview_param("molbg", "#ffffff");
		mview_param("navmode", "rot3d");
		mview_param("rendering", "wireframe");
		//mview_param("animFPS", "20");
		mview_end();
		</g:javascript>
			</td>
		</tr>
		<tr><td><b>Refined</b><br />
			<g:form>
			<g:radio value="view" checked="true"/> Protein-Ligand Complex<br />
			<g:radio value="view" /> Binding Pocket-Ligand Complex<br />
			<g:radio value="view" /> Protein Only<br />
			<g:radio value="view" /> Binding Pocket Only<br />
			</g:form>
			</td></tr>
	
		</table>
		
		</body>
		
</html>