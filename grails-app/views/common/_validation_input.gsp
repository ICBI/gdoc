<div class="validationInput">
	<g:textField name="${it.attrs.name}"  value="${fieldValue(bean: flash.cmd, field: it.attrs.name) ? fieldValue(bean: flash.cmd, field: it.attrs.name) : it.attrs.value}"  class="${hasErrors(bean:flash.cmd,field:it.attrs.name,'errors')}"/>
	<div class="errorDetail">
		<g:renderErrors bean="${flash.cmd?.errors}" field="$it.attrs.name" />
	</div>
</div>