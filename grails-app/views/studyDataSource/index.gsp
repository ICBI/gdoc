<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="portal" />
		</script>
        <title>Study DataSource</title>         
    </head>
    <body>
	<p style="font-size:14pt">Study Data Sources</p>
	<div id="centerContent" class="welcome">
		<g:panel id="studyPanel" title="My Studies" styleClass="welcome" collapse="true">
				<table class="studyTable">
					<tr>
						<th>Study Name</th>
						<th>Study ID</th>
						<th>Description</th>
						<th>Principal Investigator</th>
						<th>Cancer Type</th>
						<th>Point of Contact</th>
						<th>Level of Access</th>
					</tr>
					<g:each in="${myStudies}" var="study">
					<tr>
						<td><g:link action="show" id="${study.id}">${study.shortName}</g:link></td>
						<td>${study.id}</td>
						<td>${study.abstractText}</td>
						<td>${study.piFirstName} ${study.piLastName}, ${study.piNameSuffix}</td>
						<td>${study.cancerSite}</td>
						<td>${study.contactFirstName} ${study.contactLastName}</td>
						<td>Read</td>
					</tr>
					</g:each>
				</table>
		</g:panel>
		<br/>
		<g:panel id="studyPanel2" title="Other Studies" styleClass="welcome" collapse="true">
		<table class="studyTable">
			<tr>
				<th>Study Name</th>
				<th>Study ID</th>
				<th>Description</th>
				<th>Principal Investigator</th>
				<th>Cancer Type</th>
				<th>Point of Contact</th>
				<th>Level of Access</th>
			</tr>
			<g:each in="${otherStudies}" var="study">
			<tr>
				<td>${study.shortName}</td>
				<td>${study.id}</td>
				<td>${study.abstractText}</td>
				<td>${study.piFirstName} ${study.piLastName}, ${study.piNameSuffix}</td>
				<td>${study.cancerSite}</td>
				<td>${study.contactFirstName} ${study.contactLastName}</td>
				<td>None</td>
			</tr>
			</g:each>
		</table>
		</g:panel>
	</div>
	</body>
	
</hmtl>