package org.chai.kevin.survey

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.chai.kevin.IntegrationTests;
import org.chai.kevin.data.Type;
import org.chai.kevin.maps.MapsTarget.MapsTargetType;
import org.chai.kevin.survey.validation.SurveyEnteredObjective;
import org.chai.kevin.survey.validation.SurveyEnteredQuestion;
import org.chai.kevin.survey.validation.SurveyEnteredSection;
import org.chai.kevin.survey.validation.SurveyEnteredValue;
import org.chai.kevin.util.Utils;

abstract class SurveyIntegrationTests extends IntegrationTests {

	def newSurvey(def period) {
		return newSurvey([:], period, false)
	}
	
	def newSurvey(def names, def period) {
		return newSurvey(names, period, false)
	}
	
	def newSurvey(def names, def period, def active) {
		return new Survey(names: names, period: period, active: active).save(failOnError: true);
	}
	
	def newSurveyObjective(def survey, def order, def groups) {
		return newSurveyObjective([:], survey, order, groups)
	}
	
	def newSurveyObjective(def names, def survey, def order, def groups) {
		def objective = new SurveyObjective(names: names, survey: survey, order: order, groupUuidString: Utils.unsplit(groups)).save(failOnError: true)
		survey.addObjective(objective)
		survey.save(failOnError: true)
		return objective
	}
	
	def newSurveySection(def objective, def order, def groups) {
		def section = newSurveySection([:], objective, order, groups)
	}
	
	def newSurveySection(def names, def objective, def order, def groups) {
		def section = new SurveySection(names: names, objective: objective, order: order, groupUuidString: Utils.unsplit(groups)).save(failOnError: true)
		objective.addSection(section)
		objective.save(failOnError: true)
		return section
	}
	
	def newSurveyElement(def question, def dataElement) {
		def element = newSurveyElement(question, dataElement, [:])
	}
	
	def newSurveyElement(def question, def dataElement, def headers) {
		def element = new SurveyElement(surveyQuestion: question, dataElement: dataElement, headers: headers).save(failOnError: true)
		if (question instanceof SurveySimpleQuestion) {
			question.surveyElement = element
			question.save(failOnError: true)
		}
		return element;
	}
	
	def newSurveyEnteredValue(def element, def period, def organisationUnit, def value) {
		return new SurveyEnteredValue(surveyElement: element, value: value, organisationUnit: organisationUnit).save(failOnError: true, flush: true)
	}

	def newSurveyEnteredQuestion(def question, def period, def organisationUnit, def invalid, def complete) {
		return new SurveyEnteredQuestion(question: question, organisationUnit: organisationUnit, invalid: invalid, complete: complete).save(failOnError: true, flush: true)
	}
		
	def newSurveyEnteredSection(def section, def period, def organisationUnit, def invalid, def complete) {
		return new SurveyEnteredSection(section: section, organisationUnit: organisationUnit, invalid: invalid, complete: complete).save(failOnError: true)
	}

	def newSurveyEnteredObjective(def objective, def period, def organisationUnit, def invalid, def complete, def closed) {
		return new SurveyEnteredObjective(objective: objective, organisationUnit: organisationUnit, invalid: invalid, complete: complete, closed: closed).save(failOnError: true)
	}
	
	def newSurveyValidationRule(def element, def prefix, def groups, def expression, boolean allowOutlier, def dependencies = []) {
		def validationRule = new SurveyValidationRule(expression: expression, messages: [:], surveyElement: element, groupUuidString: Utils.unsplit(groups), dependencies: dependencies, allowOutlier: allowOutlier).save(failOnError: true)
		element.addValidationRule(validationRule)
		element.save(failOnError: true)
		return validationRule
	}
	
	def newSurveyValidationRule(def element, def prefix, def groups, def expression, def dependencies = []) {
		return newSurveyValidationRule(element, prefix, groups, expression, false, dependencies)
	}
	
	def newSkipRule(def survey, def expression, def skippedElements, def skippedQuestions) {
		def skipRule = new SurveySkipRule(survey: survey, expression: expression, skippedSurveyElements: skippedElements, skippedSurveyQuestions: skippedQuestions).save(failOnError: true)
		survey.addSkipRule(skipRule)
		survey.save(failOnError: true, flush: true)
		return skipRule
	}
	
	def newSimpleQuestion(def names, def section, def order, def groups) {
		def question = new SurveySimpleQuestion(names: names, section: section, order: order, groupUuidString: Utils.unsplit(groups)).save(failOnError: true)
		section.addQuestion(question)
		section.save(failOnError: true, flush: true)
		return question
	}
	
	def newSimpleQuestion(def section, def order, def groups) {
		return newSimpleQuestion([:], section, order, groups)
	}
	
	def newTableQuestion(def section, def order, def groups) {
		def question = new SurveyTableQuestion(section: section, order: order, groupUuidString: Utils.unsplit(groups)).save(failOnError: true)
		section.addQuestion(question)
		section.save(failOnError: true)
		return question
	}
	
	def newTableColumn(def question, def order, def groups) {
		def column = new SurveyTableColumn(question: question, order: order, groupUuidString: Utils.unsplit(groups)).save(failOnError: true)
		question.addColumn(column)
		question.save(failOnError: true)
		return column
	}
	
	def newTableRow(def question, def order, def groups, def elements) {
		def row = new SurveyTableRow(question: question, order: order, groupUuidString: Utils.unsplit(groups), surveyElements: elements).save(failOnError: true)
		question.addRow(row)
		question.save(failOnError: true)
		return row
	}
	
	def newCheckboxQuestion(def section, def order, def groups) {
		def question = new SurveyCheckboxQuestion(section: section, order: order, groupUuidString: Utils.unsplit(groups)).save(failOnError: true)
		section.addQuestion(question)
		section.save(failOnError: true)
		return question
	}
	
	def newCheckboxOption(def question, def order, def groups, def element) {
		def option = new SurveyCheckboxOption(question: question, order: order, groupUuidString: Utils.unsplit(groups), surveyElement: element).save(failOnError: true)
		question.addOption(option)
		question.save(failOnError: true)
		return option
	}

//
//	
//	def newCheckboxOption(def question)
}
