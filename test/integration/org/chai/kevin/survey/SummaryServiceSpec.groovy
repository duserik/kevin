package org.chai.kevin.survey;

import org.chai.kevin.data.Type;
import org.chai.kevin.location.DataLocationType;
import org.chai.kevin.location.DataLocation;
import org.chai.kevin.location.Location;
import org.chai.kevin.survey.summary.SurveySummaryPage;

class SummaryServiceSpec extends SurveyIntegrationTests {
	
	def summaryService
	
	def "test summary page order"() {
		setup:
		setupLocationTree()
		def period = newPeriod()
		def survey = newSurvey(period)
		def program = newSurveyProgram(survey, 1, [(HEALTH_CENTER_GROUP),(DISTRICT_HOSPITAL_GROUP)])
		def section = newSurveySection(program, 1, [(HEALTH_CENTER_GROUP),(DISTRICT_HOSPITAL_GROUP)])
		def question1 = newSimpleQuestion(section, 1, [(HEALTH_CENTER_GROUP),(DISTRICT_HOSPITAL_GROUP)])
		def element1 = newSurveyElement(question1, newRawDataElement(CODE(1), Type.TYPE_NUMBER()))
		def question2 = newSimpleQuestion(section, 2, [(HEALTH_CENTER_GROUP),(DISTRICT_HOSPITAL_GROUP)])
		def element2 = newSurveyElement(question2, newRawDataElement(CODE(2), Type.TYPE_NUMBER()))
		
		def summaryPage
		def questionSummary
		
		when:
		summaryPage = summaryService.getSurveySummaryPage(Location.findByCode(RWANDA), survey)
		questionSummary = summaryPage.getQuestionSummary(DataLocation.findByCode(KIVUYE))
		
		then:
		questionSummary.questions == 2
		questionSummary.completedQuestions == 0
		
		when:
		summaryPage = summaryService.getProgramSummaryPage(Location.findByCode(RWANDA), program)
		questionSummary = summaryPage.getQuestionSummary(DataLocation.findByCode(KIVUYE))
		
		then:
		questionSummary.questions == 2
		questionSummary.completedQuestions == 0
		
		when:
		summaryPage = summaryService.getSectionSummaryPage(Location.findByCode(RWANDA), section)
		questionSummary = summaryPage.getQuestionSummary(DataLocation.findByCode(KIVUYE))
		
		then:
		questionSummary.questions == 2
		questionSummary.completedQuestions == 0
		
		when:
		summaryPage = summaryService.getSurveySummaryPage(Location.findByCode(RWANDA), survey)
		summaryPage.sort(SurveySummaryPage.LOCATION_SORT, 'desc', 'en')
		questionSummary = summaryPage.getQuestionSummary(DataLocation.findByCode(KIVUYE))
		
		then:
		questionSummary.questions == 2
		questionSummary.completedQuestions == 0
		summaryPage.locations.equals([DataLocation.findByCode(KIVUYE), DataLocation.findByCode(BUTARO)])
		
		when:
		summaryPage = summaryService.getSurveySummaryPage(Location.findByCode(RWANDA), survey)
		summaryPage.sort(SurveySummaryPage.LOCATION_SORT, 'asc', 'en')
		questionSummary = summaryPage.getQuestionSummary(DataLocation.findByCode(KIVUYE))
		
		then:
		questionSummary.questions == 2
		questionSummary.completedQuestions == 0
		summaryPage.locations.equals([DataLocation.findByCode(BUTARO), DataLocation.findByCode(KIVUYE)])
		
		when:
		newSurveyEnteredQuestion(question1, period, DataLocation.findByCode(KIVUYE), false, true)
		summaryPage = summaryService.getSurveySummaryPage(Location.findByCode(RWANDA), survey)
		summaryPage.sort(SurveySummaryPage.PROGRESS_SORT, 'asc', 'en')
		questionSummary = summaryPage.getQuestionSummary(DataLocation.findByCode(KIVUYE))
		
		then:
		questionSummary.questions == 2
		questionSummary.completedQuestions == 1
		summaryPage.locations.equals([DataLocation.findByCode(BUTARO), DataLocation.findByCode(KIVUYE)])
	}
	
	def "test counted questions does not apply to group"() {
		setup:
		setupLocationTree()
		def period = newPeriod()
		def survey = newSurvey(period)
		def program = newSurveyProgram(survey, 1, [(HEALTH_CENTER_GROUP),(DISTRICT_HOSPITAL_GROUP)])
		def section = newSurveySection(program, 1, [(HEALTH_CENTER_GROUP),(DISTRICT_HOSPITAL_GROUP)])
		def question1 = newSimpleQuestion(section, 1, [(HEALTH_CENTER_GROUP)])
		def element1 = newSurveyElement(question1, newRawDataElement(CODE(1), Type.TYPE_NUMBER()))
		
		when:
		newSurveyEnteredQuestion(question1, period, DataLocation.findByCode(BUTARO), false, true)
		def summaryPage = summaryService.getSurveySummaryPage(Location.findByCode(RWANDA), survey)
		def questionSummary = summaryPage.getQuestionSummary(DataLocation.findByCode(BUTARO))
		
		then:
		questionSummary.questions == 0
		questionSummary.completedQuestions == 1
	}
	
	def "test locations are collected at all levels"() {
		setupLocationTree()
		def north = Location.findByCode(NORTH)
		newDataLocation(j(["en":'DP']), "DP", north, DataLocationType.findByCode(DISTRICT_HOSPITAL_GROUP));
		def period = newPeriod()
		def survey = newSurvey(period)
		
		when:
		def summaryPage = summaryService.getSurveySummaryPage(Location.findByCode(RWANDA), survey)
		
		then:
		s(summaryPage.locations*.code).equals(s([BUTARO, KIVUYE, "DP"]))
		
	}
	
}
