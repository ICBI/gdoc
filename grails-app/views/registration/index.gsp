<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main" />
	<title>G-DOC Registration</title>
</head>

<g:if test="${flash.cmd instanceof RegistrationCommand && flash.message}">
	<div class="errorDetail">${flash.message}</div>
</g:if>
<p style="font-size:14pt">Registration Form</p>
<div class="clinicalSearch">
<fieldset style="background-color:#fff;border:1px solid #334477;margin:10px 5px 5px 5px">
    <legend style="padding:7px">Georgetown NET ID required:</legend>
	<div style="padding:10px">
		<g:form name="registrationForm" action="register">
		<div class="errorDetail">
			<g:renderErrors bean="${flash.cmd?.errors}" field="netId" />
		</div>
		Enter a valid Georgetown Net-Id: <g:textField name="netId" /><br /><br />
		Select a department (optional): 
		<g:select name="department"
		          from="${departmentList}" 
				noSelection="['':'-Choose department-']"/>
		<br /><br/>
		<g:submitButton name="register" value="Register" />
		</g:form>
	</div>
</fieldset>
</div>
