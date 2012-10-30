package org.chai.kevin.planning;

import i18nfields.I18nFields;

import java.util.ArrayList
import java.util.List
import java.util.Set

import javax.persistence.Transient

import org.chai.kevin.Period
import org.chai.kevin.util.Utils

@I18nFields
class Planning {

	Period period;
	String typeCodeString;
	Boolean active = false;
	
	String names
	String overviewHelps
	String budgetHelps
	
	// deprecated
	String jsonNames
	String jsonOverviewHelps
	String jsonBudgetHelps
	
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
		
		jsonNames (nullable: true)
		jsonOverviewHelps (nullable: true)
		jsonBudgetHelps (nullable: true)
	}
	
	List<PlanningType> getAllPlanningTypes() {
		return new ArrayList(planningTypes?:[])
	}
	
	public Set<String> getTypeCodes() {
		return Utils.split(typeCodeString, Utils.DEFAULT_TYPE_CODE_DELIMITER);
	}
	
	public void setTypeCodes(Set<String> typeCodes) {
		this.typeCodeString = Utils.unsplit(typeCodes, Utils.DEFAULT_TYPE_CODE_DELIMITER);
	}
	
	public String toString(){
		return "Planning[getId()=" + getId() + ", getNames()=" + getNames() + "]";
	}

	@Transient
	public List<PlanningCost> getPlanningCosts() {
		List<PlanningCost> planningCosts = new ArrayList<PlanningCost>();
		for (PlanningType planningType : planningTypes) {
			planningCosts.addAll(planningType.getCosts());
		}
		return planningCosts;
	}
}
