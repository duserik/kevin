<%@ page import="org.chai.kevin.data.Enum" %>
<g:if test="${type.enumCode != null}">
	<g:set var="enume" value="${Enum.findByCode(type.enumCode)}"/>
</g:if>

<!-- Enum type question -->
<div id="element-${surveyElement.id}-${suffix}" class="element element-enum ${enteredValue?.isSkipped(suffix)?'skipped':''} ${(enteredValue==null || enteredValue?.isValid(suffix))?'':'errors'}" data-element="${surveyElement.id}" data-suffix="${suffix}">
	<a name="element-${surveyElement.id}-${suffix}"></a>
   	<g:if test="${!print}">
	   	<g:if test="${lastValue!=null}">
			<g:set var="option" value="${enume?.getOptionForValue(lastValue.enumValue)}"/>
			<g:set var="tooltipValue" value="${option!=null?i18n(field: option.names):lastValue.enumValue}"/>
		</g:if>

		<select class="${tooltipValue!=null?'tooltip':''} input ${!readonly?'loading-disabled':''}" ${tooltipValue!=null?'title="'+tooltipValue+'"':''} name="surveyElements[${surveyElement.id}].value${suffix}" disabled="disabled">
			<option value=""><g:message code="survey.element.enum.select.label"/></option>
			<!-- TODO fix this, there should be a flag in the survey, not on the element directly -->
			<g:each in="${enume?.activeEnumOptions}" var="option">
				<option value="${option.value}"  ${option?.value==value?.enumValue ? 'selected':''}>
					<g:i18n field="${option.names}" />
				</option>
			</g:each>
		</select>
	</g:if>
	<g:else>
	<label>-- <g:message code="survey.print.selectonlyoneoption.label" default="Select only one response"/> --</label>
		<g:each in="${enume?.activeEnumOptions}" var="option">
			<div>
				<input class="input" type="checkbox" value="1" name="option.names" ${option?.value==value?.enumValue? 'checked="checked" ':''} disabled="disabled"/>
				<span><g:i18n field="${option.names}" /></span>
			</div>
		</g:each>
	</g:else>
	<div class="error-list">
		<g:renderUserErrors element="${enteredValue}" suffix="${suffix}"/>
	</div>
</div>
