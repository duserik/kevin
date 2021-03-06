package org.chai.kevin.planning;

import groovy.transform.EqualsAndHashCode;
import i18nfields.I18nFields

import javax.persistence.Transient

import org.chai.kevin.Period
import org.chai.kevin.util.DataUtils

@I18nFields
//@EqualsAndHashCode(includes='id')
class Planning {

	Period period;
	String typeCodeString;
	Boolean active;
	
	String names
	String overviewHelps
	String budgetHelps
	
	static i18nFields = ['names', 'overviewHelps', 'budgetHelps']
	
	static hasMany = [
	     planningTypes: PlanningType,
		 skipRules: PlanningSkipRule,
		 planningOutputs: PlanningOutput             
	]
	
	static mapping = {
		table 'dhsst_planning'
		planningTypes cascade: "all-delete-orphan"
		skipRules cascade: "all-delete-orphan"
		planningOutputs cascade: "all-delete-orphan"
		period column: 'period'
	}
	
	static constraints = {
		period (nullable: false)
		names (nullable: true)
		overviewHelps (nullable: true)
		budgetHelps (nullable: true)
		typeCodeString (nullable: true)
	}
	
	List<PlanningType> getAllPlanningTypes() {
		return new ArrayList(planningTypes?:[])
	}
	
	public Set<String> getTypeCodes() {
		return DataUtils.split(typeCodeString, DataUtils.DEFAULT_TYPE_CODE_DELIMITER);
	}
	
	public void setTypeCodes(Set<String> typeCodes) {
		this.typeCodeString = DataUtils.unsplit(typeCodes, DataUtils.DEFAULT_TYPE_CODE_DELIMITER);
	}
	
	@Override
	public String toString() {
		return "Planning [period=" + period + ", active=" + active + "]";
	}

	@Transient
	public List<PlanningCost> getPlanningCosts() {
		List<PlanningCost> planningCosts = new ArrayList<PlanningCost>();
		for (PlanningType planningType : planningTypes) {
			planningCosts.addAll(planningType.getAllCosts());
		}
		return planningCosts;
	}
	
}
