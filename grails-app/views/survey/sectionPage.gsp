<%@ page import="org.chai.kevin.survey.SurveyPage.SectionStatus" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="main" />
		<title><g:message code="surveyPage.section.label" default="District Health System Portal" />
		</title>
	</head>
	<body>
		<g:set var="closed" value="${surveyPage.getStatus(surveyPage.section) == SectionStatus.CLOSED}"/>
		<g:set var="unavailable" value="${surveyPage.getStatus(surveyPage.section) == SectionStatus.UNAVAILABLE}"/>
		<g:set var="readonly" value="${closed||unavailable}"/>
	
		<div id="survey">
			<g:render template="/survey/header" model="[period: surveyPage.period, organisation: surveyPage.organisation, objective: surveyPage.objective]"/>
			
			<div id="bottom-container">
				<g:render template="/survey/menu" model="[surveyPage: surveyPage]"/>
				
				<div id="survey-right-question-container" class="box">

					<g:if test="${flash.message}">
						<div class="rounded-box-top rounded-box-bottom info">
							<g:message code="${flash.message}" default="${flash.default}"/>
						</div>
					</g:if>
								
					<g:if test="${closed}">
						<div class="rounded-box-top rounded-box-bottom">
							This section has been submitted, you can view your answer here but you cannot change them.
						</div>
					</g:if> 
					<g:if test="${unavailable}">
						<div class="rounded-box-top rounded-box-bottom">
							This section can not yet be answered, please complete 
							<a href="${createLink(controller: 'survey', action: 'objectivePage', params: [organisation: surveyPage.organisation.id, objective: surveyPage.objective.dependency.id])}"><g:i18n field="${surveyPage.objective.dependency.names}"/></a>
							first.
						</div>
					</g:if> 
					
					<g:if test="${!closed&&!unavailable}">
						<div class="rounded-box-top">
							<h5>
								<g:i18n field="${surveyPage.section.names}" />
							</h5>
						</div>
						<div class="rounded-box-bottom">
						
							<g:form id="survey-form" url="[controller:'survey', action:'save', params: [organisation: surveyPage.organisation.id, section: surveyPage.section.id, survey: surveyPage.survey.id]]" useToken="true">
								<g:set var="i" value="${1}" />
								<ol id="questions">
									<g:each in="${surveyPage.section.getQuestions(surveyPage.organisation.organisationUnitGroup)}" var="question">
										<li class="question-container ${surveyPage.isSkipped(question)?'skipped':''}">
											<g:render template="/survey/question/${question.getType()}" model="[question: question, surveyPage: surveyPage, readonly: readonly, number: i++]" />
										</li> 
									</g:each>
								</ol>
								
								<g:if test="${!closed}">
									<button type="submit">Save</button>
								</g:if>
							</g:form>
						</div>
					</g:if>
					<div class="clear"></div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
		
			$(document).ready(function() {
				$('#survey-form').delegate('input, select, textarea', 'change', function(){
					surveyValueChanged(this, valueChangedInSection);
				});
				$('#survey-form').delegate('a.outlier-validation', 'click', function(){
					$(this).next().val($(this).data('rule')); surveyValueChanged(this, valueChangedInSection);
					return false;
				});
			});
		
			function valueChangedInSection(data, element) {
				$('.question').each(function(key, question) {
					var valid = true;
					$(data.invalidQuestions).each(function(key, invalidQuestion) {
						if (invalidQuestion.id == $(question).data('question')) {
							$(question).parents('.question-container').html(invalidQuestion.html);
							valid = false;
						}
					});
					if (valid) {
						$(question).parents('.question-container').find('.error-list').remove()
						$(question).parents('.question-container').find('.errors').removeClass('errors');
					}
				});
					
				$('.question').each(function(key, element) {
					if ($.inArray($(element).data('question'), data.skippedQuestions) >= 0) {
						$(element).parents('.question-container').addClass('skipped');
					}
					else {
						$(element).parents('.question-container').removeClass('skipped');
					}
				});
				
				$('.element').each(function(key, element) {
					if ($.inArray($(element).data('element'), data.skippedElements) >= 0) {
						$(element).addClass('skipped');
					}
					else {
						$(element).removeClass('skipped');
					}
				});
			}
		</script>
	</body>
</html>