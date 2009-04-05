<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
		</script>
        <title>Search Clinical Data</title>         
    </head>
    <body>
		<jq:plugin name="tooltip"/>
		<g:javascript>
			$(document).ready(function (){
				$('.info').tooltip({showURL: false});
				$('.close').click(function() {
					$(this).parents('.clinicalSearch').hide('slow');
				});
			});
		</g:javascript>
	<p style="font-size:14pt">Search Clinical Data</p>
	<div id="centerContent">
		<br/>
		<g:form name="searchForm" action="search">
			<g:each in="${dataTypes}">
				<div class="clinicalSearch">
					<div style="float: left">
						${it.longName} 	
						<img class="info" title="${it.definition}" src="${createLinkTo(dir:'images',file:'information.png')}" border="0" />
					</div>
				<div style="float: right; vertical-align: middle">
					<img class="close"src="${createLinkTo(dir:'images',file:'cross.png')}" border="0" />
				</div>
				<br/>
				<br/>
					<g:if test="${it.vocabulary}">
						<g:select name="clinical_${it.shortName}" 
								noSelection="${['':'Select One...']}"
								from="${it.vocabs}" optionKey="term" optionValue="termMeaning">
						</g:select>
					</g:if>
					<g:else>
						<g:textField name="clinical_${it.shortName}"  />
					</g:else>
				</div>
			</g:each>
			<br/>
			<br/>
			<g:submitButton name="submit" value="Search"/>
		</g:form>
	</div>
	</body>
	
</hmtl>