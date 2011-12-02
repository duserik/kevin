<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="ajax" />
        <g:set var="entityName" value="${message(code: 'dashboard.explanation.label', default: 'Data element explanation')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    
    <body>

		<div class="box">
			<div><g:i18n field="${rawDataElement.names}"/></div>
			<div class="row">Type: <span class="type"><g:toHtml value="${rawDataElement.type.getDisplayedValue(2, null)}"/></span></div>
			<div><g:i18n field="${rawDataElement.descriptions}"/></div>
			<div class="clear"></div>
		</div>
		
		<g:if test="${surveyElements.size()!=0}">
			<table class="listing">
				<thead>
					<tr>
						<th><g:message code="period.label" default="Iteration"/></th>
						<th><g:message code="survey.label" default="Survey"/></th>
						<th><g:message code="survey.question.label" default="Question"/></th>
						<th><g:message code="dataelement.surveyelement.facility.applicable.label" default="Total Number of Facility Applicable"/></th>
					</tr>
				</thead>
				<tbod>
					<g:each in="${surveyElements}" status="i" var="surveyElement"> 
						<g:set var="question" value="${surveyElement.key.surveyQuestion}" /> 
						<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
							<td>${question.section.objective.survey.period.startDate} &harr; ${question.section.objective.survey.period.endDate}</td>
							<td>${i18n(field:question.section.objective.survey.names)}</td>
							<td><g:stripHtml field="${question.names}" chars="100"/></a></td>
							<td>${surveyElement.value}</td>
						</tr>
					</g:each>
				</tbod>
			</table>
		</g:if>
		<g:else>
			No Survey Element Associated to This Data Element
		</g:else>

		<table class="listing">
			<thead>
				<tr>
					<th><g:message code="period.label" default="Iteration"/></th>
					<th><g:message code="default.number.label" args="[message(code:'datavalue.label')]" default="Number of Data Value"/></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${periodValues}" status="i" var="periodValue"> 
					<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
						<td>${periodValue.key.startDate} &harr; ${periodValue.key.endDate}</td>
						<td>${periodValue.value}</td>
					</tr>
				</g:each>
			</tbody>
		</table>
		
		<g:if test="${!referencingData.isEmpty()}">
			<g:render template="/entity/data/referencingDataList" model="[referencingData: referencingData]"/>
		</g:if>
		
	</body>
</html>
