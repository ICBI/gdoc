<html>
<head>
	<title>Genome Browser</title>  
	<link rel="stylesheet" href="${createLinkTo(dir: 'css/genomeBrowser',  file: 'genome.css')}"/>
	<link rel="stylesheet" href="${createLinkTo(dir: 'css/genomeBrowser',  file: 'tundra.css')}"/>
	<link rel="stylesheet" href="${createLinkTo(dir: 'js/dojo/resources',  file: 'dojo.css')}"/>
	<link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'superfish.css')}"/>
    <link rel="stylesheet" href="${createLinkTo(dir: 'css',  file: 'styles.css')}"/>

    <script type="text/javascript" src="${createLinkTo(dir: 'js/dojo',  file: 'dojo.js')}" djConfig="isDebug: false"></script>
    <script type="text/javascript" src="${createLinkTo(dir: 'js/dojo',  file: 'jbrowse_dojo.js')}" ></script>

    <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'jbrowse.js')}" ></script>

    <!-- <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'Browser.js')}" ></script>
 	<script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'Track.js')}" ></script>
 	<script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'UITracks.js')}" ></script>
 	<script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'GenomeView.js')}" ></script>
	<script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'Util.js')}" ></script>
 	<script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'NCList.js')}" ></script>
 	<script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'LazyPatricia.js')}" ></script>
 	<script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'SequenceTrack.js')}" ></script>
	<script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'FeatureTrack.js')}" ></script>
 	<script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'ImageTrack.js')}" ></script>
 	<script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'Layout.js')}" ></script> -->
	<g:javascript library="jquery"/>
	<jq:plugin name="tooltip"/>
	<jq:plugin name="blockui" />
	<jq:plugin name="livequery"/>
    <script type="text/javascript">
    /* <![CDATA[ */
		jQuery.noConflict();


			dojo.subscribe("noFeature", function(data) {
				jQuery.growlUI('Error', 'No feature with identifier \'' + data + '\' found.'); 
			});
			dojo.subscribe("tracksChanged", function(data) {
				jQuery('.patientTooltip').each(function() {
					if(!jQuery(this).data('tooltip'))
						jQuery(this).tooltip({showURL: false});
					jQuery(this).parent('.tracklist-label, .track-label').each(function() {
						jQuery(this).removeClass('track-label tracklist-label');
						jQuery(this).addClass('patient-label');
					});
				});
				
			});
			function clickFeature(event) {
				var elem = (event.currentTarget || event.srcElement);
                //depending on bubbling, we might get the subfeature here
                //instead of the parent feature
                if (!elem.feature) elem = elem.parentElement;
                if (!elem.feature) return; //shouldn't happen; just bail if it does
                var feat = elem.feature;
				var featureType = getFeatureType(elem);
				var id = feat[4];
				switch(featureType) {
					case 'snp': 
						handleSnp(id);
						break;
					case 'omim':
						handleOmim(id);
						break;
					case 'refseq':
						handleRefseq(id);
						break;
					case 'mirna':
						handleMirna(id);
						break;
					case 'genes':
						handleGene(id);
						break;						
					default:
						break;
				}

			}
			
			function getFeatureType(element) {
				var tracks = jQuery(element).parents('.track');
				if(tracks)
					return tracks[0].id.substring(tracks[0].id.indexOf('_') + 1);
			}
			
			function handleSnp(id) {
				window.open("http://www.ncbi.nlm.nih.gov/projects/SNP/snp_ref.cgi?rs=" + id.substring(id.indexOf('rs') + 2));
			}
			
			function handleOmim(id) {
				window.open("http://www.ncbi.nlm.nih.gov/omim/" + id);
			}
			
			function handleRefseq(id) {
				window.open("http://www.ncbi.nlm.nih.gov/sites/entrez?cmd=search&db=gene&term=" + id);
			}
			
			function handleMirna(id) {
				if(id.indexOf('hsa') > -1)
					window.open("http://www.mirbase.org/cgi-bin/query.pl?terms=" + id);
			}
			
			function handleGene(id) {
				window.open("http://www.genecards.org/cgi-bin/carddisp.pl?gene=" + id);
			}
		   var trackInfo = ${session.tracks}
		   var refSeqs = ${session.sequences}
           var queryParams = dojo.queryToObject(window.location.search.slice(1));
           var bookmarkCallback = function(brwsr) {
               return window.location.protocol
                      + "//" + window.location.host
                      + window.location.pathname
                      + "?loc=" + brwsr.visibleRegion()
                      + "&tracks=" + brwsr.visibleTracks();
           }
           var b = new Browser({
                                   containerID: "GenomeBrowser",
                                   refSeqs: refSeqs,
                                   trackData: trackInfo,
                                   location: '${session.browseLocation}',
                                   tracks: 'DNA,ChromosomeBand,${session.showTracks}',
								   browserRoot: "${createLinkTo(dir: '')}/"
                               });
		
    /* ]]> */
    </script>	       
</head>
<body>
	<div id="doc3" class="yui-t1">
	<div id="hd" style="overflow:hidden;">
	    <!-- Header start -->
	    <g:render template="/common/header" plugin="gcore"/>
		
	    <!-- Header end -->
	</div>
	<div class="c" style="background:#fff;border:.5px solid #000;">
		<div>
		<g:render template="/common/nav_top" plugin="gcore"/>
		<br/>
		<br/>
	<br/>
    <div id="GenomeBrowser" style="height: 100%; width: 100%; padding: 0; border: 0;"></div>
	</div>
</body>

</hmtl>