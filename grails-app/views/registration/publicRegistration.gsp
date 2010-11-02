<html>
<head>
	<meta name="layout" content="splash" />
	<title>G-DOC Public Registration</title>
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
	
<fieldset style="background-color:#fff;border:1px solid #334477;margin:10px 5px 5px 5px">
    <legend style="padding:7px">Request access to G-DOC:</legend>
	<div style="padding:10px;float:left">
		<g:form name="registrationPublicForm" action="registerPublic">
		<div class="errorDetail">
			<g:renderErrors bean="${flash.cmd?.errors}"/>
		</div>
		Enter a valid email address (userId): <g:textField name="userId" value="${flash.cmd?.userId}" /><br /><br />
		
		<recaptcha:ifEnabled>
		    <recaptcha:recaptcha theme="blackglass"/>
		</recaptcha:ifEnabled>
		<br /><br/>
		<g:submitButton name="registerPublic" value="Register" />
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
