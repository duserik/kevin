package org.chai.kevin.survey

import org.chai.kevin.data.Type;
import org.chai.kevin.survey.export.SurveyExportData
import org.chai.kevin.survey.export.SurveyExportDataPoint
import org.chai.kevin.util.Utils;
import org.hisp.dhis.organisationunit.OrganisationUnit;

class SurveyExportServiceSpec extends SurveyIntegrationTests {

	def surveyExportService
	
	def "test for export section"(){
		setup:
		setupOrganisationUnitTree()
		def period = newPeriod()
		def survey = newSurvey(j(["en":"survey"]), period)
		def objective = newSurveyObjective(j(["en":"objective"]), survey, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def section = newSurveySection(j(["en":"section"]), objective, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def question = newSimpleQuestion(j(["en":"question"]), section, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def type = Type.TYPE_NUMBER()
		def element = newSurveyElement(question, newDataElement(CODE(1), type))
		newSurveyEnteredValue(element, period, OrganisationUnit.findByName(BUTARO), v("10"))
		
		when:
		def surveyExportData = surveyExportService.getSurveyExportData(getOrganisation(BUTARO), section, objective, survey)
		List<SurveyExportDataPoint> dataPoints = surveyExportData.getDataPoints();
	
		then:
		dataPoints.size() == 1
		dataPoints.get(0).equals(["survey",NORTH,BURERA,BUTARO,DISTRICT_HOSPITAL_GROUP,"objective","section","SIMPLE","NUMBER","question","10.0"])
	}

	def "test for export objective"(){
		setup:
		setupOrganisationUnitTree()
		def period = newPeriod()
		def survey = newSurvey(j(["en":"survey"]), period)
		def objective = newSurveyObjective(j(["en":"objective"]), survey, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def section = newSurveySection(j(["en":"section"]), objective, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def question = newSimpleQuestion(j(["en":"question"]), section, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def type = Type.TYPE_NUMBER()
		def element = newSurveyElement(question, newDataElement(CODE(1), type))
		newSurveyEnteredValue(element, period, OrganisationUnit.findByName(BUTARO), v("10"))
		
		when:
		def surveyExportData = surveyExportService.getSurveyExportData(getOrganisation(BUTARO), section, objective, survey)
		List<SurveyExportDataPoint> dataPoints = surveyExportData.getDataPoints();
	
		then:
		dataPoints.size() == 1
		dataPoints.get(0).equals(["survey",NORTH,BURERA,BUTARO,DISTRICT_HOSPITAL_GROUP,"objective","section","SIMPLE","NUMBER","question","10.0"])
	}

	def "test for export survey"(){
		setup:
		setupOrganisationUnitTree()
		def period = newPeriod()
		def survey = newSurvey(j(["en":"survey"]), period)
		def objective = newSurveyObjective(j(["en":"objective"]), survey, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def section = newSurveySection(j(["en":"section"]), objective, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def question = newSimpleQuestion(j(["en":"question"]), section, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def type = Type.TYPE_NUMBER()
		def element = newSurveyElement(question, newDataElement(CODE(1), type))
		newSurveyEnteredValue(element, period, OrganisationUnit.findByName(BUTARO), v("10"))
		
		when:
		def surveyExportData = surveyExportService.getSurveyExportData(getOrganisation(BUTARO), section, objective, survey)
		List<SurveyExportDataPoint> dataPoints = surveyExportData.getDataPoints();
	
		then:
		dataPoints.size() == 1
		dataPoints.get(0).equals(["survey",NORTH,BURERA,BUTARO,DISTRICT_HOSPITAL_GROUP,"objective","section","SIMPLE","NUMBER","question","10.0"])
	}
	
	def "test for skip levels"(){
		setup:
		setupOrganisationUnitTree()
		def period = newPeriod()
		def survey = newSurvey(j(["en":"survey"]), period)
		def objective = newSurveyObjective(j(["en":"objective"]), survey, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def section = newSurveySection(j(["en":"section"]), objective, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def question = newSimpleQuestion(j(["en":"question"]), section, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def type = Type.TYPE_NUMBER()
		def element = newSurveyElement(question, newDataElement(CODE(1), type))
		newSurveyEnteredValue(element, period, OrganisationUnit.findByName(BUTARO), v("10"))
		
		when:
		def surveyExportData = surveyExportService.getSurveyExportData(getOrganisation(BUTARO), section, objective, survey)
		List<SurveyExportDataPoint> dataPoints = surveyExportData.getDataPoints();
	
		then:
		dataPoints.size() == 1		
		!dataPoints.get(0).equals(["survey",COUNTRY,NORTH,BURERA,SECTOR,BUTARO,DISTRICT_HOSPITAL_GROUP,"objective","section","SIMPLE","NUMBER","question","10.0"])
		dataPoints.get(0).equals(["survey",NORTH,BURERA,BUTARO,DISTRICT_HOSPITAL_GROUP,"objective","section","SIMPLE","NUMBER","question","10.0"])
	}
	
	def "test for simple question with multiple list headers"() {
		setup:
		setupOrganisationUnitTree()
		def period = newPeriod()
		def survey = newSurvey(j(["en":"survey"]), period)
		def objective = newSurveyObjective(j(["en":"objective"]), survey, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def section = newSurveySection(j(["en":"section"]), objective, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def question = newSimpleQuestion(j(["en":"question"]), section, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def type = Type.TYPE_LIST(Type.TYPE_MAP(["key1":Type.TYPE_NUMBER()]))
		def element = newSurveyElement(question, newDataElement(CODE(1), type), ['[_].key1':j(['en':'header1'])])
		newSurveyEnteredValue(element, period, OrganisationUnit.findByName(BUTARO), type.getValue([['key1':10]]))
		
		when:
		def surveyExportData = surveyExportService.getSurveyExportData(getOrganisation(BUTARO), section, objective, survey)
		List<SurveyExportDataPoint> dataPoints = surveyExportData.getDataPoints();
	
		then:
		dataPoints.size() == 1
		dataPoints.get(0).equals(["survey",NORTH,BURERA,BUTARO,DISTRICT_HOSPITAL_GROUP,"objective","section","SIMPLE","LIST","question",
			"10.0","header1"])
	}
	
	def "test for when no questions are active for a facility type"(){
		setup:
		setupOrganisationUnitTree()
		def period = newPeriod()
		def survey = newSurvey(j(["en":"survey"]), period)
		def objective = newSurveyObjective(j(["en":"objective"]), survey, 1, [(HEALTH_CENTER_GROUP)])
		def section = newSurveySection(j(["en":"section"]), objective, 1, [(HEALTH_CENTER_GROUP)])
		def question = newSimpleQuestion(j(["en":"question"]), section, 1, [(HEALTH_CENTER_GROUP)])
		def type = Type.TYPE_LIST(Type.TYPE_MAP(["key1":Type.TYPE_NUMBER()]))
		def element = newSurveyElement(question, newDataElement(CODE(1), type), ['[_].key1':j(['en':'header1'])])
		newSurveyEnteredValue(element, period, OrganisationUnit.findByName(BUTARO), type.getValue([['key1':10]]))

		when:
		def surveyExportData = surveyExportService.getSurveyExportData(getOrganisation(BUTARO), section, objective, survey)
		List<SurveyExportDataPoint> dataPoints = surveyExportData.getDataPoints();
	
		then:
		dataPoints.size() == 0
	}
	
	def "test for get zip file"(){
		setup:
		setupOrganisationUnitTree()
		def period = newPeriod()
		def survey = newSurvey(j(["en":"survey"]), period)
		def objective = newSurveyObjective(j(["en":"objective"]), survey, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def section = newSurveySection(j(["en":"section"]), objective, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def question = newSimpleQuestion(j(["en":"question"]), section, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def type = Type.TYPE_NUMBER()
		def element = newSurveyElement(question, newDataElement(CODE(1), type))
		newSurveyEnteredValue(element, period, OrganisationUnit.findByName(BUTARO), v("10"))
		
		when:
		def file = surveyExportService.getSurveyExportFile(getOrganisation(BUTARO), section, objective, survey)
		def zipFile = Utils.getZipFile(file)
		
		then:
		zipFile.exists() == true
		zipFile.length() > 0
	}
	
	def "test for valid export filename"() {
		setup:
		setupOrganisationUnitTree()
		def period = newPeriod()
		def survey = newSurvey(j(["en":"survey"]), period)
		def objective = newSurveyObjective(j(["en":"objective"]), survey, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def section = newSurveySection(j(["en":"section"]), objective, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def question = newSimpleQuestion(j(["en":"question"]), section, 1, [(DISTRICT_HOSPITAL_GROUP)])
		def type = Type.TYPE_NUMBER()
		def element = newSurveyElement(question, newDataElement(CODE(1), type))
		newSurveyEnteredValue(element, period, OrganisationUnit.findByName(BUTARO), v("10"))
		
		when:
		def file = surveyExportService.getSurveyExportFile(getOrganisation(BUTARO), section, objective, survey)
		def zipFile = Utils.getZipFile(file)
		def zipFileName = zipFile.getName()
		
		then:
		zipFileName.startsWith("section_ButaroDH_")
	}
		
}
