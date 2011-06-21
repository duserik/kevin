package org.chai.kevin.maps

import org.chai.kevin.AbstractReportController;
import org.chai.kevin.Organisation;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.hisp.dhis.organisationunit.OrganisationUnitLevel;
import org.hisp.dhis.period.Period;

class MapsController extends AbstractReportController {

	def mapsService
	
    def index = {
		redirect (action: 'view', params: params)
	}
	
	def view = {
		if (log.isDebugEnabled()) log.debug("maps.view, params:"+params)
		
		Period period = getPeriod()
		MapsTarget target = getMapsTarget()
		Organisation organisation = getOrganisation(true)
		
		Integer organisationLevel = ConfigurationHolder.config.facility.level;
		
		[
			periods: Period.list(), 
			targets: MapsTarget.list(),
			organisationTree: organisationService.getOrganisationTreeUntilLevel(organisationLevel.intValue()-1),
			currentPeriod: period, 
			currentTarget: target,
			currentOrganisation: organisation
		]
	}
	
	def explain = {
		if (log.isDebugEnabled()) log.debug("maps.infos, params:"+params)
		
		Period period = getPeriod()
		Organisation organisation = getOrganisation(true)
		MapsTarget target = getMapsTarget()
		
		MapsExplanation explanation = mapsService.getExplanation(period, organisation, target);
		[explanation: explanation]
	}
	
	def map = {
		if (log.isDebugEnabled()) log.debug("maps.map, params:"+params)
		
		Period period = getPeriod()
		Organisation organisation = getOrganisation(true)
		MapsTarget target = getMapsTarget()
		Integer level = getOrganisationUnitLevel()
		
		org.chai.kevin.maps.Maps map = mapsService.getMap(period, organisation, level, target);
		
		if (log.isDebugEnabled()) log.debug("displaying map: "+map)		
		render(contentType:"text/json", text:'{"result":"success","map":'+map.toJson()+'}');
	}

}
