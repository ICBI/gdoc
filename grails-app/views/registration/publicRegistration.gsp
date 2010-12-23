<html>
<head>
	<meta name="layout" content="splash" />
	<title>G-DOC Public Registration</title>
	<g:javascript library="jquery" />
</head>

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
		<span style="font-size:1.05em">A registration link will be sent your email address after submission</span><br />
		 If you do not receive a registration email in a timely manner, <br />
		 check your 'spam' box and verify your filter does not block future email from gdoc-help@georgetown.edu<br />
	</div>
</fieldset>
</div>
