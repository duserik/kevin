package org.chai.kevin.maps;

/* 
 * Copyright (c) 2011, Clinton Health Access Initiative.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chai.kevin.LocationService;
import org.chai.kevin.data.Info;
import org.chai.kevin.data.InfoService;
import org.chai.kevin.location.CalculationEntity;
import org.chai.kevin.location.DataEntityType;
import org.chai.kevin.location.LocationEntity;
import org.chai.kevin.location.LocationLevel;
import org.chai.kevin.value.CalculationValue;
import org.chai.kevin.value.ValueService;
import org.hisp.dhis.period.Period;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public class MapsService {

	private static final Log log = LogFactory.getLog(MapsService.class);
	
	private LocationService locationService;
	private ValueService valueService;
	private InfoService infoService;
	
	public Maps getMap(Period period, LocationEntity entity, LocationLevel level, MapsTarget target) {
		if (log.isDebugEnabled()) log.debug("getMap(period="+period+",entity="+entity+",target="+target+")");

		List<Polygon> polygons = new ArrayList<Polygon>();
		List<LocationLevel> levels = locationService.listLevels();
		
		if (target == null) return new Maps(period, target, entity, level, polygons, levels);
		List<LocationEntity> organisations = locationService.getChildrenOfLevel(entity, level);

		for (CalculationEntity child : organisations) {
			Double value = null;
			// TODO groups
			CalculationValue<?> calculationValue = valueService.getCalculationValue(target.getCalculation(), child, period, new HashSet<DataEntityType>(locationService.listTypes()));
			if (calculationValue != null) {
				if (!calculationValue.getValue().isNull()) {
					value = calculationValue.getValue().getNumberValue().doubleValue();
				}
			}
			
			polygons.add(new Polygon(child, value));
		}

		return new Maps(period, target, entity, level, polygons, levels);
	}

	public Info<?> getExplanation(Period period, CalculationEntity entity, MapsTarget target) {
		// TODO groups
		return infoService.getCalculationInfo(target.getCalculation(), entity, period, new HashSet<DataEntityType>(locationService.listTypes()));
	}
	
	public void setValueService(ValueService valueService) {
		this.valueService = valueService;
	}
	
	public void setLocationService(LocationService locationService) {
		this.locationService = locationService;
	}
	
	public void setInfoService(InfoService infoService) {
		this.infoService = infoService;
	}
	
}
