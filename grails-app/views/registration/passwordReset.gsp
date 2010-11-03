<html>
<head>
	<meta name="layout" content="splash" />
	<title>G-DOC Password Reset</title>
	<g:javascript library="jquery" />
</head>


<%--div style="margin:0px auto;text-align:center;width:50%;border:1px solid gray">
	<p style="font-size:14pt">Registration Form</p><br />
	<span>
		G-DOC registration has been restricted to Georgetown users with valid NetIds for the G-DOC application. Public registration will begin in the next G-DOC release.
	</span>
</div--%>


<div class="clinicalSearch" style="width:85%;margin:0 auto">
	
<g:if test="${flash.message}">
		<div class="message">${flash.message}</div>
</g:if>
<g:if test="${flash.error}">
		<div class="errorDetail">${flash.error}</div>
</g:if>

<g:if test="${netId}">
		<div class="errorDetail">It appears you have a netId, you cannot change your password using this page, and will need to do so via the Georgetown Help Desk</div>
</g:if>
	
<fieldset style="background-color:#fff;border:1px solid #334477;margin:10px 5px 5px 5px">
    <legend style="padding:7px">Password reset:</legend>
	<div style="padding:10px;float:left">
		<g:form name="resetForm" action="resetLoginCredentials">
		<div class="errorDetail">
			<g:renderErrors bean="${flash.cmd?.errors}"/>
		</div>
		<g:if test="${session.userId}">
		User ID: ${session.userId} <g:hiddenField name="userId" value="${session.userId}"/><br /><br />
		</g:if>
		<g:else>
		Enter a user id (email address): <g:textField name="userId" value="${flash.cmd?.userId}" /><br /><br />
		</g:else>
		
		<recaptcha:ifEnabled>
		    <recaptcha:recaptcha theme="blackglass"/>
		</recaptcha:ifEnabled>
		<br /><br/>
		<g:submitButton name="requestReset" value="Request password reset" />
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
