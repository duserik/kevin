<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ajax" />
        <g:set var="entityName" value="${message(code: 'maps.explanation.label', default: 'Maps explanation')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
		<div class="explanation">
			<h3><g:i18n field="${target.names}"/> in ${info.organisationUnit.name}</h3>
			<g:if test="${info != null}">
				<g:render template="${info.template}" model="[info: info, groups: groups]"/>
			</g:if>
			<g:else>
				<div class="red bold">No expression available for this facility type.</div>
			</g:else>
		</div>
    </body>
</html>