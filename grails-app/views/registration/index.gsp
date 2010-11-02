<html>
<head>
	<meta name="layout" content="splash" />
	<title>G-DOC Registration</title>
	<g:javascript>
	$(document).ready( function () {
				$('#userCat').change(function() {
					if($('#userCat').val() == 'Georgetown User') {
						$('#netIdReg').css('display','block');
						$('#publicReg').css('display','none');
					}else{
						$('#publicReg').css('display','block');
						$('#netIdReg').css('display','none');
					}
		 		});
	});
	</g:javascript>
</head>

<div id="userCatDiv" align="center" style="padding:10px">
<span style="font-size:13pt;">Select your user category</span><br />
<g:select id="userCat" from="${categoryList}" noSelection="${['null':'Select One...']}" />
</div>

<g:if test="${flash.cmd instanceof RegistrationCommand && flash.message}">
	<div class="errorDetail">${flash.message}</div>
</g:if>
<%--div style="margin:0px auto;text-align:center;width:50%;border:1px solid gray">
	<p style="font-size:14pt">Registration Form</p><br />
	<span>
		G-DOC registration has been restricted to beta-testers for the G-DOC application as of July 26, 2010. It will reopen when G-DOC formally released. 
	</span>
</div--%>


<div class="clinicalSearch" id="netIdReg" style="width:85%;margin:0 auto;display:none">
	
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

<div class="clinicalSearch" id="publicReg" style="width:85%;margin:0 auto;display:none">
	
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
