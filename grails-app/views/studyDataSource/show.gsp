<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="portal" />
		</script>
        <title>${session.study.shortName} Details</title>         
    </head>
    <body>
	<p style="font-size:14pt">${session.study.shortName} Details</p>
	<div id="centerContent" class="welcome">
		<g:panel id="studyPanel" title="Study Details" styleClass="welcome" >
		<table class="studyTable" width="100%">
			<tr>
				<td width="20%">Study Name</td>
				<td>${session.study.shortName}</td>
			</tr>
			<tr>
				<td>Study Abstract</td>
				<td>${session.study.abstractText}</td>
			</tr>			
			<tr>
				<td>Principal Investigator</td>
				<td>${session.study.piFirstName} ${session.study.piLastName}, ${session.study.piNameSuffix}</td>
			</tr>
			<tr>
				<td>Cancer Type</td>
				<td>${session.study.cancerSite}</td>
			</tr>			
			<tr>
				<td>Point of Contact</td>
				<td>${session.study.contactFirstName} ${session.study.contactLastName}</td>
			</tr>
		</table>
		</g:panel>
		<br/>
		<g:panel id="studyPanel" title="Data Type Details" styleClass="welcome" >
		<table class="studyTable" width="100%">
			<tr>
				<th>Data Type</th>
				<th>Number of Elements</th>
				<th>Search</th>
			</tr>
			<tr>
				<td>Clinical Data</td>
				<td>${clinicalElements.size} Clinical Elements</td>
				<td><g:link controller="clinical">Search</g:link></td>
			</tr>			
			<tr>
				<td>Genomic Data</td>
				<td>2,000 Genes</td>
				<td><g:link controller="genomic">Search</g:link></td>
			</tr>			
		</table>
		</g:panel>
	</div>
	</body>
	
</hmtl>