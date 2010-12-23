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
	$('#activateAccountForm').submit(function() {
	      if ($("#consent:checked").val() !== undefined) {
	        return true;
	      }
	      $("#consentSpan").text("Please agree to Legal Term of Use").show().fadeOut(5000);
	      return false;
	 });
});	
</g:javascript>

<div class="clinicalSearch" style="width:85%;margin:0 auto">

<fieldset style="background-color:#fff;border:1px solid #334477;margin:10px 5px 5px 5px">
    <legend style="padding:7px">Activate G-DOC account:</legend>
	<div class="c" style="border:1px solid silver;padding:10px;margin-right:10px;font-size:.8em">
		<span style="font-size:1.05em">Your password must meet the following requirements:</span><br />
		 * It must be between 8 and 15 characters long.<br />
		 * It must have at least one number.<br />
		 * It must have at least one letter.<br />
		 * It must have at least one symbol (!,@,#,$,^).
	</div>
	<div style="padding:10px;">
		
		<div style="padding-left:25px">
		<g:if test="${flash.message}">
				<div class="message">${flash.message}</div>
		</g:if>
		<div class="errorDetail">
			<g:renderErrors bean="${flash.cmd?.errors}"/>
		</div>
		<br />
		</div>

		<g:form name="activateAccountForm" id="activateAccountForm" action="activateAccount">
		<span style="font-weight:bold">User ID</span>: ${userId}
		<g:hiddenField name="userId" value="${userId}"/><br /><br />
		<span style="font-weight:bold">*Enter password:</span> <g:passwordField name="password" id="passwordField" /><br /><br />
		<span style="font-weight:bold">*First Name:</span> <g:textField name="firstName" id="firstNameField" /><br /><br />
		<span style="font-weight:bold">*Last Name:</span> <g:textField name="lastName" id="lastNameField" /><br /><br />
		<span style="font-weight:bold">*Organization/Affiliation:</span> <g:textField name="organization" id="organizationField" /><br /><br />
		
		<br />
		<div style="font-style:italic;"><span style="font-weight:bold">*</span> Required field</div>
		<br/>
		<span id="consentSpan" style="color:red"></span><br />
		<g:checkBox name="consent" id="consent" class="consent" value="${false}" />&nbsp;&nbsp;&nbsp;I agree to legal terms of use<br />
		<br />
		<g:submitButton name="activateAccount" value="Activate account" />
		</g:form>
	</div>
	
</fieldset>
</div>
