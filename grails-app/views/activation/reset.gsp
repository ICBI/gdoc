<html>
<head>
	<meta name="layout" content="splash" />
	<title>G-DOC Password Reset</title>
	<g:javascript library="jquery" />
	<jq:plugin name="password"/>
</head>
<g:javascript>
$(document).ready(function() {
	$('#passwordField').pstrength();
});	
</g:javascript>

<div class="clinicalSearch" style="width:85%;margin:0 auto">

<div style="width:55%;">	
<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
</g:if>
<div class="errorDetail"><g:renderErrors bean="${flash.cmd?.errors}"/></div>
</div>
	
<fieldset style="background-color:#fff;border:1px solid #334477;margin:10px 5px 5px 5px">
    <legend style="padding:7px">Reset password for G-DOC account:</legend>
	<div style="padding:10px;float:left">
		<g:form name="resetPasswordForm" action="resetPassword">
		
		User ID: ${userId}
		<g:hiddenField name="userId" value="${userId}"/><br /><br />
		Enter password: <g:passwordField name="password" id="passwordField" /><br /><br />
		
		<br /><br/>
		<g:submitButton name="resetPassword" value="Reset password" />
		</g:form>
	</div>
	<div class="c" style="float:right;border:1px solid silver;padding:10px;margin-right:10px">
		<span style="font-size:1.05em">Your password must meet the following requirements:</span><br />
		 * It must be at least eight characters long.<br />
		 * It must have at least one number.<br />
		 * It must have at least one letter.<br />
		 * It must have at least one symbol (!,@,#,$,^).
	</div>
</fieldset>
</div>
