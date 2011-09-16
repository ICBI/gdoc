(function( $ ){
	
	var settings = {
		'advancedMenu': false,
		'kmForm': "#geneExpressionKm",
		'geneExpressionForm': "#geneExpression",
		'multiple': false,
		'menuType': 'gene'
	};
	var methods = {
		init : function( options ) { 
			if ( options ) { 
				$.extend( settings, options );
			}
			if(options.multiple) {
				$('#contextmenu' + settings.menuType).remove();
			}
			var menu = "<ul id='contextmenu" + settings.menuType + "' class='jqcontextmenu'>";
			switch(settings.menuType) {
				case 'gene':
					if(settings.advancedMenu) {
						if(settings.kmData.length > 0) {
							menu += "<li><a>Perform Gene Expression KM</a><ul>";
							$(settings.kmData).each(function() {
								menu += "<li><a href='#' class='contextItem' id='KM'>Endpoint: " + this[0] + "</a></li>";
							});
							menu += "</ul></li>";
						} 
						menu += "<li><a href='#' class='contextItem' id='GENEEXPRESSION'>Perform Gene Expression Search</a></li>";
					}
					menu += "<li><a href='#' class='contextItem' id='ENTREZ'>Search in Entrez</a></li>	\
						<li><a href='#' class='contextItem' id='IHOP'>Search in iHOP</a></li>	\
						<li><a href='#' class='contextItem' id='PIR'>Search in PIR</a></li>	\
						<li><a href='#' class='contextItem' id='ENSEMBL'>Search in Ensembl Gene View</a></li>	\
						<li><a href='#' class='contextItem' id='REACTOME'>Search in Reactome</a></li>	\
						<li><a href='#' class='contextItem' id='KEGG'>View at KEGG</a></li>	\
						<li><a href='#' class='contextItem' id='QUICKGO'>View at QuickGO</a></li>	\
						<li><a href='#' class='contextItem' id='GENECARDS'>View at GeneCards</a></li>	\
						<li><a href='#' class='contextItem' id='STRING'>View at String DB</a></li></ul>";
					break;
				case 'microrna':
					menu += "<li><a href='#' class='contextItem' id='MIRBASE'>View in miRBase</a></li> \
						<li><a href='#' class='contextItem' id='ENTREZ'>Search in Entrez</a></li>	\
						<li><a href='#' class='contextItem' id='IHOP'>Search in iHOP</a></li></ul>";
					break;
				case 'copynumber':
					menu += "<li><a href='#' class='contextItem' id='UCSCGenomeBrowser'>View in UCSC Genome Browser</a></li></ul>";
					break;
				default:
					break;
			}
			
			$(menu).appendTo('body');
			$(".contextItem").unbind('click', handler);
			$(".contextItem").bind('click', handler);
		}
	};
	
	function handler(e) {
		var source = e.target.id;
		var id = jquerycontextmenu.currentTarget.html();
		jquerycontextmenu.hidebox($, $('.jqcontextmenu'));
		switch(source) {
			case "ENTREZ":
				window.open("http://www.ncbi.nlm.nih.gov/sites/gquery?GlobalQuery.GQueryCluster.GQuerySearchBox.Term=" + id);
				break;
			case "IHOP":
				window.open("http://www.ihop-net.org/UniPub/iHOP/?search=" + id + "&field=all&ncbi_tax_id=9606");
				break;
			case "PIR":
				window.open('http://pir.georgetown.edu/cgi-bin/textsearch.pl?field0=GENENAME_TXT&query0=' + id + '&andor1=AND&field1=TAXID&query1=9606&opt_dsp_list2=PRONAME,LENGTH,ORGNAME,SUPFAM,BSCE_5,MATCHED&numPerPg=50&search=1');
				break;
			case "ENSEMBL":
				window.open('http://uswest.ensembl.org/Homo_sapiens/Search/Results?species=Homo_sapiens;idx=;q=' + id);
				break;
			case "REACTOME":
				window.open('http://www.reactome.org/cgi-bin/search2?OPERATOR=ALL&SPECIES=48887&QUERY=' + id);
				break;
			case "KEGG":
				window.open("http://www.kegg.jp/dbget-bin/www_bget?hsa:" + id);
				break;
			case "QUICKGO":
				window.open("http://www.ebi.ac.uk/QuickGO/GSearch?q=" + id + "&x=33&y=10#1=2");
				break;		
			case "GENECARDS":
				window.open("http://www.genecards.org/cgi-bin/carddisp.pl?gene=" + id);
				break;
			case "STRING":
				window.open('http://string81.embl.de/interactionsList/' + id + '&caller_identity=genecards&target_mode=proteins&network_flavor=evidence&input_query_species=9606');
				break;
			case "MIRBASE":
				window.open("http://www.mirbase.org/cgi-bin/query.pl?terms=" + id);
				break;
			case "UCSCGenomeBrowser":
				if (id.indexOf('p') == -1 && id.indexOf('q') == -1) {
					id = "chr"+id
				}
				window.open('http://genome.ucsc.edu/cgi-bin/hgTracks?clade=mammal&org=Human&db=hg18&position=' + id + '&hgt.suggest=&pix=800&Submit=submit');
				break;
			case "KM":
				if(settings.spinner) {
					$(settings.spinner).css("visibility","visible");
				}
				var kmAtt;
				var att = $(e.target).html();
				if (settings.kmData.length > 1) {
					$(settings.kmData).each(function() {
						if(("Endpoint: " + this[0]) == att) {
							kmAtt = this[1];
						}
					});
				} else {
					kmAtt = settings.kmData[0][1];
				}
				$(settings.kmForm + " > input[name=geneName]").val(id);
				$(settings.kmForm + " > input[name=endpoint]").val(kmAtt);
				$(settings.kmForm).submit();
				break;
			case "GENEEXPRESSION":
				if(settings.spinner) {
					$(settings.spinner).css("visibility","visible");
				}
				$(settings.geneExpressionForm + " > input[name=geneName]").val(id);
				$(settings.geneExpressionForm).submit();
				break;						
			default:
				alert("Error!  Source not found.");
		}
		return false;
	}
  $.fn.geneLink = function(options) {
	methods.init.apply( this, arguments );

    return this.each(function() {     
		var $this = $(this)   
		if(!options.menuType)
			$(this).addcontextmenu('contextmenugene');
		else 
			$(this).addcontextmenu('contextmenu' + options.menuType);
	});

  };
})( jQuery );