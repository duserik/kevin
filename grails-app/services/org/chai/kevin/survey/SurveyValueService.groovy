package org.chai.kevin.survey;

import org.apache.shiro.SecurityUtils
import org.chai.kevin.OrganisationService
import org.chai.kevin.survey.validation.SurveyEnteredObjective
import org.chai.kevin.survey.validation.SurveyEnteredQuestion
import org.chai.kevin.survey.validation.SurveyEnteredSection
import org.chai.kevin.survey.validation.SurveyEnteredValue
import org.hibernate.Criteria
import org.hibernate.FlushMode
import org.hibernate.criterion.Projections
import org.hibernate.criterion.Restrictions
import org.hisp.dhis.organisationunit.OrganisationUnit


class SurveyValueService {
	
	static transactional = true
	
	def sessionFactory;
	
	private OrganisationService organisationService;
	
	void save(SurveyEnteredObjective surveyEnteredObjective) {
		if (log.isDebugEnabled()) log.debug("save(surveyEnteredObjective=${surveyEnteredObjective}})")
		surveyEnteredObjective.setUserUuid(SecurityUtils.subject.principal)
		surveyEnteredObjective.setTimestamp(new Date());
		surveyEnteredObjective.save();
	}

	void delete(SurveyEnteredObjective surveyEnteredObjective) {
		surveyEnteredObjective.delete()
	}
	
	void save(SurveyEnteredValue surveyEnteredValue) {
		if (log.isDebugEnabled()) log.debug("save(surveyEnteredValue=${surveyEnteredValue}})")
		surveyEnteredValue.setUserUuid(SecurityUtils.subject.principal)
		surveyEnteredValue.setTimestamp(new Date());
		surveyEnteredValue.save();
	}
	
	void delete(SurveyEnteredValue surveyEnteredValue) {
		surveyEnteredValue.delete()
	}
	
	void save(SurveyEnteredQuestion surveyEnteredQuestion) {
		if (log.isDebugEnabled()) log.debug("save(surveyEnteredQuestion=${surveyEnteredQuestion}})")
		surveyEnteredQuestion.setUserUuid(SecurityUtils.subject.principal)
		surveyEnteredQuestion.setTimestamp(new Date());
		surveyEnteredQuestion.save();
	}
	
	void delete(SurveyEnteredQuestion surveyEnteredQuestion) {
		surveyEnteredQuestion.delete()
	}
	
	void save(SurveyEnteredSection surveyEnteredSection) {
		if (log.isDebugEnabled()) log.debug("save(surveyEnteredSection=${surveyEnteredSection}})")
		surveyEnteredSection.setUserUuid(SecurityUtils.subject.principal)
		surveyEnteredSection.setTimestamp(new Date());
		surveyEnteredSection.save();
	}
	
	void delete(SurveyEnteredSection surveyEnteredSection) {
		surveyEnteredSection.delete()
	}
	
	Integer getNumberOfSurveyEnteredObjectives(Survey survey, OrganisationUnit organisationUnit, Boolean closed, Boolean complete, Boolean invalid) {
		def c = SurveyEnteredObjective.createCriteria()
		c.add(Restrictions.eq("organisationUnit", organisationUnit))
		
		if (complete!=null) c.add(Restrictions.eq("complete", complete))
		if (invalid!=null) c.add(Restrictions.eq("invalid", invalid))
		if (closed!=null) c.add(Restrictions.eq("closed", closed))
		
		c.createAlias("objective", "o").add(Restrictions.eq('o.survey', survey))
		c.setProjection(Projections.rowCount())
		c.setCacheable(false);
		c.setFlushMode(FlushMode.COMMIT)
		c.uniqueResult();
	}
	
	Integer getNumberOfSurveyEnteredQuestions(Survey survey, OrganisationUnit organisationUnit, 
		SurveyObjective objective, SurveySection section, Boolean complete, Boolean invalid) {
		def c = SurveyEnteredQuestion.createCriteria()
		c.add(Restrictions.eq("organisationUnit", organisationUnit))
		
		if (complete!=null) c.add(Restrictions.eq("complete", complete))
		if (invalid!=null) c.add(Restrictions.eq("invalid", invalid))
		
		c.createAlias("question", "sq")
		.createAlias("sq.section", "ss")
		.createAlias("ss.objective", "so")
		.add(Restrictions.eq("so.survey", survey))
		
		if (section != null) c.add(Restrictions.eq("sq.section", section))
		if (objective != null) c.add(Restrictions.eq("ss.objective", objective))
		
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		c.setProjection(Projections.rowCount())
		c.setCacheable(false);
		c.setFlushMode(FlushMode.COMMIT)
		c.uniqueResult();
	}
	
	SurveyEnteredSection getSurveyEnteredSection(SurveySection surveySection, OrganisationUnit organisationUnit) {
		def c = SurveyEnteredSection.createCriteria()
		c.add(Restrictions.naturalId()
			.set("organisationUnit", organisationUnit)
			.set("section", surveySection)
		)
		
		c.setFlushMode(FlushMode.COMMIT)
		def result = c.uniqueResult();
		if (log.isDebugEnabled()) log.debug("getSurveyEnteredSection(...)="+result);
		return result
	}
	
	SurveyEnteredObjective getSurveyEnteredObjective(SurveyObjective surveyObjective, OrganisationUnit organisationUnit) {
		def c = SurveyEnteredObjective.createCriteria()
		c.add(Restrictions.naturalId()
			.set("organisationUnit", organisationUnit)
			.set("objective", surveyObjective)
		)
		c.setFlushMode(FlushMode.COMMIT)
		
		def result = c.uniqueResult();
		if (log.isDebugEnabled()) log.debug("getSurveyEnteredObjective(...)="+result);
		return result
	}

	SurveyEnteredQuestion getSurveyEnteredQuestion(SurveyQuestion surveyQuestion, OrganisationUnit organisationUnit) {
		def c = SurveyEnteredQuestion.createCriteria()
		c.add(Restrictions.naturalId()
			.set("organisationUnit", organisationUnit)
			.set("question", surveyQuestion)
		)
		
		c.setFlushMode(FlushMode.COMMIT)
		def result = c.uniqueResult();
		if (log.isDebugEnabled()) log.debug("getSurveyEnteredQuestion(...)="+result);
		return result
	}
	
	SurveyEnteredValue getSurveyEnteredValue(SurveyElement surveyElement, OrganisationUnit organisationUnit) {
		def c = SurveyEnteredValue.createCriteria()
		c.add(Restrictions.naturalId()
			.set("organisationUnit", organisationUnit)
			.set("surveyElement", surveyElement)
		)
		c.setCacheable(true)
		c.setCacheRegion("surveyEnteredValueQueryCache")
		
		c.setFlushMode(FlushMode.COMMIT)
		def result = c.uniqueResult();
		if (log.isDebugEnabled()) log.debug("getSurveyEnteredValue(...)="+result);
		return result
	}
	
	List<SurveyEnteredValue> getSurveyEnteredValues(OrganisationUnit organisationUnit, SurveySection section, SurveyObjective objective, Survey survey) {
		
		def c = SurveyEnteredValue.createCriteria()
		c.add(Restrictions.eq("organisationUnit", organisationUnit))
		
		if (survey != null || objective != null || section != null) c.createAlias("surveyElement", "se")
		if (survey != null || objective != null || section != null) c.createAlias("se.surveyQuestion", "sq")
		if (survey != null || objective != null) c.createAlias("sq.section", "ss")
		if (survey != null) c.createAlias("ss.objective", "so")

		if (section != null) c.add(Restrictions.eq("sq.section", section))
		if (objective != null) c.add(Restrictions.eq("ss.objective", objective))
		if (survey != null) c.add(Restrictions.eq("so.survey", survey))
		
		def result = c.setFlushMode(FlushMode.COMMIT).list();
		if (log.isDebugEnabled()) log.debug("getSurveyEnteredValue(...)="+result);
		return result				
	}

}
