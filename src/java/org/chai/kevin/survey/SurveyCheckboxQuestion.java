/** 
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
package org.chai.kevin.survey;

/**
 * @author JeanKahigiso
 *
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.chai.kevin.util.Utils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hisp.dhis.organisationunit.OrganisationUnitGroup;

@SuppressWarnings("serial")
@Entity(name = "SurveyCheckboxQuestion")
@Table(name = "dhsst_survey_checkbox_question")
public class SurveyCheckboxQuestion extends SurveyQuestion {

	List<SurveyCheckboxOption> options = new ArrayList<SurveyCheckboxOption>();
	
    @OneToMany(targetEntity=SurveyCheckboxOption.class, mappedBy="question")
    @Cascade({CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    @OrderBy(value="order")
    @Fetch(FetchMode.SELECT)
	public List<SurveyCheckboxOption> getOptions() {
		return options;
	}

	public void setOptions(List<SurveyCheckboxOption> options) {
		this.options = options;
	}

	public void addCheckboxOption(SurveyCheckboxOption option) {
		option.setQuestion(this);
		options.add(option);
		Collections.sort(options);
	}
	
    @Transient
	@Override
	public String getType() {
		String gspName = "checkboxQuestion";
		return gspName;
	}
    
    @Transient
	@Override
	public List<SurveyElement> getSurveyElements(OrganisationUnitGroup group) {
		List<SurveyElement> dataElements = new ArrayList<SurveyElement>();
		for (SurveyCheckboxOption option : getOptions(group)) {
			dataElements.add(option.getSurveyElement());
		}
		return dataElements;
	}
    
    @Transient
	public List<SurveyCheckboxOption> getOptions(OrganisationUnitGroup group) {
		List<SurveyCheckboxOption> result = new ArrayList<SurveyCheckboxOption>();
		for (SurveyCheckboxOption surveyCheckboxOption : getOptions()) {
			if (Utils.getGroupUuids(surveyCheckboxOption.getGroupUuidString()).contains(group.getUuid())) result.add(surveyCheckboxOption);
		}
		return result;
	}

}
