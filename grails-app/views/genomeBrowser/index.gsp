<html>
<head>
	<meta name="layout" content="main" />
	<g:javascript library="jquery" />
	<jq:plugin name="blockui" />
	<script type="text/javascript" src="${createLinkTo(dir: 'js/dojo',  file: 'dojo.js')}" djConfig="isDebug: false"></script>
	<script type="text/javascript" src="${createLinkTo(dir: 'js/genomeBrowser',  file: 'LazyPatricia.js')}" ></script>
	<title>GDOC Genome Browser</title>         
</head>
<body>
<g:javascript>
	var names = new LazyTrie("/content/jbrowse/names/lazy-",
			      "/content/jbrowse/names/root.json");
	$(document).ready(function() {
		toggleFeature();
		toggleBlock($('#omicsData'));
		$('#omicsData').click(function() {
			toggleBlock(this);
		});
		$('#submit').click(function() {
			if($('#searchType:checked').val() == 'feature')
				search($('#feature').val());
			else
				$('#browseForm').submit();
		})
		$('.searchRadio').click(function() {
			toggleFeature();
		})
	});
	
	function toggleBlock(item) {
		if($(item).attr("checked")) {
			$('#omicsDiv').unblock();
		} else {
			$('#omicsDiv').block({
				message: null
			});
		}
	}
	
	function toggleFeature() {
		if($('#searchType:checked').val() == 'feature') {
			$('#locationSearch').hide();
			$('#geneSearch').show();
		} else {
			$('#geneSearch').hide();
			$('#locationSearch').show();
		}
	}
	
	function search(item) {
		dojo.subscribe("noFeature", function(data) {
			$('#noFeature').show();
		});
		names.exactMatch(item, function(matches) {
		    var goingTo;
		    //first check for exact case match
		    for (var i = 0; i < matches.length; i++) {
				if (matches[i][1] == item) {
			    	goingTo = matches[i];
				}
		    }
		    //if no exact case match, try a case-insentitive match
	       	if (!goingTo) {
	        	for (var i = 0; i < matches.length; i++) {
	        		if (matches[i][1].toLowerCase() == item.toLowerCase())
	                	goingTo = matches[i];
	        	}
	        }	
	
            //else just pick a match
	    	if (!goingTo) goingTo = matches[0];
	    	var startbp = goingTo[3];
	    	var endbp = goingTo[4];
	    	var flank = Math.round((endbp - startbp) * .2);
			$('#trackMatch').val(names.extra[matches[0][0]]);
			$('#hiddenLocation').val(goingTo[2] + ":" + (startbp - flank)  + ".." + (endbp + flank));
			$('#browseForm').submit();
			
		});

		
	}
</g:javascript>
<p style="font-size:14pt">Genome Browser</p>
<br/>
<div>
	<g:form name="browseForm" action="view">
		<div class="clinicalSearch">
			Select Location Criteria<br/><br/>
			<g:radio name="searchType" value="feature" checked="${!flash.cmd || (flash.cmd?.searchType == 'feature')}" class="searchRadio"/> Browse to Feature (Enter RefSeq ID, dbSNP ID, miRNA ID)<br/><br/>
			<g:radio name="searchType" value="location" checked="${flash.cmd?.searchType == 'location'}" class="searchRadio"/> Browse to Chromosome Location <br/><br/>
			<div id="geneSearch" class="search">
				Enter Feature ID: <g:textField name="feature"/><br/>
				<div id="noFeature" class="errorDetail" style="display:none;">No features by that name found.  Please try again.</div><br/>
			</div>
			<div id="locationSearch" class="search" style="display: none">
				Enter Chromosome: 
				<g:select name="chromosome" from="${session.chromosomes}">
				</g:select>
				Location: 
				<g:textField name="location"/><br/>
				<div class="errorDetail">
					<g:renderErrors bean="${flash.cmd?.errors}" field="location" />
				</div>
				<br/>
			</div>
			<g:hiddenField name="hiddenLocation"/>
			<g:hiddenField name="trackMatch"/>
			<g:hiddenField name="omicsTypes"/>
			<g:checkBox name="omicsData" value="${flash.cmd?.omicsData}"/> Add Omics Data to Display<br/><br/>
		</div>
	</g:form>
		
		<div class="clinicalSearch" id="omicsDiv" style="height: 200px">
			<div id="studyPicker">
				<g:render template="/studyDataSource/studyPicker" model="[remote:true]"  plugin="gcore"/>
			</div>
			<div class="errorDetail">
				<g:renderErrors bean="${flash.cmd?.errors}" field="omicsTypes" />
			</div>
			<div id="searchDiv">
				<g:render template="studyForm"/>
			</div>
				
		</div>
		<br/>
		<g:submitButton name="submit" value="Submit"/>
</div>

</body>

</hmtl>