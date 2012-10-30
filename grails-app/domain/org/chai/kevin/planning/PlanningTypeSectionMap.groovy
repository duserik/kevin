package org.chai.kevin.planning

import i18nfields.I18nFields;

import java.util.Map;

@I18nFields
class PlanningTypeSectionMap implements Serializable {
	
	String section
	String names
	
	// deprecated
	String jsonNames
	
	static i18nFields = ['names']
	
	static belongsTo = [planningType: PlanningType]
	
	static transients = ['namesMap']
	
	static mapping = {
		table 'dhsst_planning_type_descriptions'
		id composite: ['section', 'planningType']
		
		header column: 'sectionDescriptions_KEY'
		planningType column: 'PlanningType'
		version false
	}
	
	static constraints = {
		section (nullable: false)
		names (nullable: true)
		
		jsonNames (nullable: true)
	}
	
	Map<String, String> getNamesMap() {
		Map result = [:]
		result.getMetaClass().get = {String language -> return getNames(new Locale(language))}
		return result
	}
	
	void setNamesMap(Map<String, String> namesMap) {
		namesMap.each {
			setNames(it.value, new Locale(it.key))
		}
	}
}
