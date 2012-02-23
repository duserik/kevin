<div class="entity-form-container togglable">
	<div class="entity-form-header">
		<h3 class="title">
			<g:message code="default.new.label" args="[message(code:'planningType.label',default:'Planning')]"/>
		</h3>
		<g:locales/>
		<div class="clear"></div>
	</div>
	<g:form url="[controller:'planningType', action:'save', params:[targetURI: targetURI]]" useToken="true">
		<input type="hidden" name="planning.id" value="${planningType.planning.id}"/>
	
		<g:i18nInput name="names" bean="${planningType}" value="${planningType.names}" label="Name" field="names"/>
		<g:i18nInput name="namesPlural" bean="${planningType}" value="${planningType.namesPlural}" label="Name (plural)" field="namesPlural"/>

		<g:input name="discriminator" bean="${planningType}" value="${planningType.discriminator}" label="Discriminator" field="discriminator"/>
		
		<g:selectFromList name="dataElement.id" label="Data element" bean="${planningType}" field="dataElement" optionKey="id" multiple="false"
			ajaxLink="${createLink(controller:'data', action:'getAjaxData', params:[class:'DataElement'])}"
			from="${dataElements}" value="${planningType.dataElement?.id}" values="${dataElements.collect{i18n(field:it.names)+' ['+it.code+'] ['+it.class.simpleName+']'}}" />
	
		<g:if test="${headerPrefixes != null && !headerPrefixes.empty}">
			<div class="row ${hasErrors(bean:planningType, field:'headers', 'errors')}">
				<a href="#" onclick="$(this).next().toggle();return false;"><g:message code="planning.planningType.headers.label" default="Headers"/>:</a> 
				<div class="hidden">
					<g:each in="${headerPrefixes}" var="headerPrefix">
						<input type="hidden" name="headerList" value="${headerPrefix}"/>
						<g:i18nRichTextarea name="headerList['${headerPrefix}']" bean="${planningType}" value="${planningType.headers[headerPrefix]}" label="${headerPrefix}" field="headers" height="50"/>
					</g:each>
				</div>
			</div>
		</g:if>
		
		<g:if test="${sections != null && !sections.empty}">
			<div class="row ${hasErrors(bean:planningType, field:'sectionDescriptions', 'errors')}">
				<a href="#" onclick="$(this).next().toggle();return false;"><g:message code="planning.planningType.sections.label" default="Section descriptions"/>:</a> 
				<div class="hidden">
					<g:each in="${sections}" var="section">
						<input type="hidden" name="sectionList" value="${section}"/>
						<g:i18nRichTextarea name="sectionList[${section}]" bean="${planningType}" value="${planningType.sectionDescriptions[section]}" label="${section}" field="sectionDescriptions" height="50"/>
					</g:each>
				</div>
			</div>
		</g:if>
		
		<g:if test="${planningType.id != null}">
			<input type="hidden" name="id" value="${planningType.id}"></input>
		</g:if>
		<div class="row">
			<button type="submit" class="rich-textarea-form"><g:message code="default.button.save.label" default="Save"/></button>&nbsp;&nbsp;
			<a href="${createLink(uri: targetURI)}"><g:message code="default.link.cancel.label" default="Cancel"/></a>
		</div>
    </g:form>
	<div class="clear"></div>
</div>