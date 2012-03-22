<div class="entity-form-container">
	
	<div class="entity-form-header">
		<h3 class="title">
			<g:message code="default.new.label" args="[message(code:'user.label')]"/>
		</h3>
		<g:locales/>
	</div>
	<div class="data-field-column">
		<g:form url="[controller:'user', action:'save', params: [targetURI: targetURI]]" useToken="true">
			<g:input name="username" label="${message(code:'user.username.label')}" bean="${user}" field="username"/>
			<g:input name="firstname" label="${message(code:'user.firstname.label')}" bean="${user}" field="firstname"/>
			<g:input name="lastname" label="${message(code:'user.lastname.label')}" bean="${user}" field="lastname"/>
			<g:input name="location" label="${message(code:'user.location.label')}" bean="${user}" field="location"/>
			<g:input name="email" label="${message(code:'user.email.label')}" bean="${user}" field="email"/>
			<g:input name="permissionString" label="${message(code:'user.permission.label')}" bean="${user}" field="permissionString"/>
			<g:input name="password" label="${message(code:'user.password.label')}" type="password" bean="${cmd}" field="password"/>
			<g:input name="repeat" label="${message(code:'user.repeatpassword.label')}" type="password" bean="${cmd}"  field="repeat"/>
						
			<div class="row">
				<label><g:message code="user.confirmed.label"/></label>
				<g:checkBox name="confirmed" value="${user.confirmed}" />
			</div>
			
			<div class="row">
				<label><g:message code="user.active.label"/></label>
				<g:checkBox name="active" value="${user.active}" />
			</div>
			
			<g:selectFromList name="roles" label="${message(code:'user.roles.label')}" bean="${user}" field="roles" 
				from="${roles}" value="${user.roles*.id}" optionValue="name" optionKey="id" multiple="true"/>
			
			<g:if test="${user.id != null}">
				<input type="hidden" name="id" value="${user.id}"/>
			</g:if>
			
			<div class="row">
				<button type="submit"><g:message code="default.button.save.label"/></button>
				<a href="${createLink(uri: targetURI)}"><g:message code="default.link.cancel.label"/></a>
			</div>
		</g:form>
	</div>
</div>
