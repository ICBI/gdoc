<html>
<head>
	<meta name="layout" content="splash" />
	<title>G-DOC Account Activation</title>
	<g:javascript library="jquery" />
	<jq:plugin name="password"/>
</head>
<g:javascript>
$(document).ready(function() {
	$('#passwordField').pstrength();
});	
</g:javascript>

<div class="clinicalSearch" style="width:85%;margin:0 auto">
	
<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
</g:if>
<div class="errorDetail">
	<g:renderErrors bean="${flash.cmd?.errors}"/>
</div>
	
<fieldset style="background-color:#fff;border:1px solid #334477;margin:10px 5px 5px 5px">
    <legend style="padding:7px">Activate G-DOC account:</legend>
	<div style="padding:10px;float:left">
		<g:form name="activateAccountForm" action="activateAccount">
		User ID: ${userId}
		<g:hiddenField name="userId" value="${userId}"/><br /><br />
		Enter password: <g:passwordField name="password" id="passwordField" /><br /><br />
		First Name: <g:textField name="firstName" id="firstNameField" /><br /><br />
		Last Name: <g:textField name="lastName" id="lastNameField" /><br /><br />
		Organization: <g:textField name="organization" id="organizationField" /><br /><br />
		
		<br /><br/>
		<g:submitButton name="activateAccount" value="Activate account" />
		</g:form>
	</div>
	<div class="c" style="float:right;border:1px solid silver;padding:10px;margin-right:10px">
		<span style="font-size:1.05em">Your password must meet the following requirements:</span><br />
		 * It must be between 8 and 15 characters long.<br />
		 * It must have at least one number.<br />
		 * It must have at least one letter.<br />
		 * It must have at least one symbol (!,@,#,$,^).
	</div>
</fieldset>
</div>
