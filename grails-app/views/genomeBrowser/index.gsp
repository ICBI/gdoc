<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>Genome Browser</title>  
	<link rel="stylesheet" href="${createLinkTo(dir: 'css/genomeBrowser',  file: 'genome.css')}"/>
	<link rel="stylesheet" href="${createLinkTo(dir: 'css/genomeBrowser',  file: 'tundra.css')}"/>
	<link rel="stylesheet" href="${createLinkTo(dir: 'js/dojo/resources',  file: 'dojo.css')}"/>

    <script type="text/javascript" src="${createLinkTo(dir: 'js/dojo',  file: 'dojo.js')}" djConfig="isDebug: false"></script>
    <script type="text/javascript" src="${createLinkTo(dir: 'js/dojo',  file: 'jbrowse_dojo.js')}" ></script>

    <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'Browser.js')}" ></script>
    <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'Util.js')}" ></script>
    <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'NCList.js')}" ></script>
    <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'LazyPatricia.js')}" ></script>
    <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'Track.js')}" ></script>
    <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'SequenceTrack.js')}" ></script>
    <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'FeatureTrack.js')}" ></script>
    <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'StaticTrack.js')}" ></script>
    <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'ImageTrack.js')}" ></script>
    <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'GenomeView.js')}" ></script>
    <script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'Layout.js')}" ></script>

    <script type="text/javascript">
    /* <![CDATA[ */
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
                                   defaultTracks: "CopyNumber",
                                   location: queryParams.loc,
                                   tracks: queryParams.tracks,
                                   bookmark: bookmarkCallback
                               });
    /* ]]> */
    </script>	       
</head>
<body>
    <div id="GenomeBrowser" style="height: 100%; width: 100%; padding: 0; border: 0;"></div>
</body>

</hmtl>