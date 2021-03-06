package org.chai.kevin.dashboard

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

import org.chai.kevin.data.Calculation
import org.chai.kevin.data.Summ
import org.chai.kevin.data.Type
import org.chai.kevin.reports.ReportProgram

class DashboardTargetControllerSpec extends DashboardIntegrationTests {

	def dashboardTargetController
	def locationService
	def dashboardService
	def dataService
	
	def "delete target"() {
		setup:
		def root = newReportProgram(CODE(1))
		def ratio = newSum("1", CODE(2))
		def target = newDashboardTarget(TARGET1, ratio, root, 1)
		dashboardTargetController = new DashboardTargetController();
		dashboardTargetController.dataService = dataService
		
		when:
		dashboardTargetController.params.id = target.id
		dashboardTargetController.delete()
		
		then:
		dashboardTargetController.response.redirectedUrl.equals(dashboardTargetController.getTargetURI())
		ReportProgram.count() == 1
		DashboardTarget.count() == 0
	}
	
	def "save target with calculations"() {
		setup:
		def ratio = newSum("1", CODE(2))
		def root = newReportProgram(CODE(1))
		def target = newDashboardTarget(TARGET1, ratio, root, 1)
		dashboardTargetController = new DashboardTargetController()
		dashboardTargetController.dataService = dataService
		
		when:
		dashboardTargetController.params['id'] = target.id
		dashboardTargetController.params['weight'] = 1
		dashboardTargetController.params['code'] = "NEW"
		dashboardTargetController.params['data.id'] = ratio.id+""
		dashboardTargetController.saveWithoutTokenCheck()
		def newTarget = DashboardTarget.findByCode("NEW")
		
		then:
		dashboardTargetController.response.redirectedUrl.equals(dashboardTargetController.getTargetURI())
		newTarget != null
		newTarget.calculation.equals(ratio)
		newTarget.weight == 1
		DashboardTarget.count() == 1
		Summ.count() == 1
	}
	
	def "edit target with calculations"() {
		setup:
		setupLocationTree()
		def root = newReportProgram(CODE(1))
		def ratio = newSum("1", CODE(3))
		def target = newDashboardTarget(TARGET1, ratio, root, 1)
		dashboardTargetController = new DashboardTargetController()
		dashboardTargetController.dataService = dataService
		
		when:
		dashboardTargetController.params['id'] = target.id
		dashboardTargetController.params['weight'] = 1
		dashboardTargetController.params['code'] = "NEW"
		dashboardTargetController.params['data.id'] = ratio.id+""
		dashboardTargetController.saveWithoutTokenCheck()
		def newTarget = DashboardTarget.findByCode("NEW")
		
		then:
		dashboardTargetController.response.redirectedUrl.equals(dashboardTargetController.getTargetURI())
		newTarget != null
		newTarget.calculation.equals(ratio)
		newTarget.weight == 1
		DashboardTarget.count() == 1
		Summ.count() == 1
	}
	
	def "search target"() {
		setup:
		def ratio = newSum("1", CODE(2))
		def root = newReportProgram(CODE(1))
		def target = newDashboardTarget(TARGET1, ratio, root, 1)
		dashboardTargetController = new DashboardTargetController()
		
		when:
		dashboardTargetController.params.q = TARGET1
		dashboardTargetController.search()
		
		then:
		dashboardTargetController.modelAndView.model.entities.size() == 1
		dashboardTargetController.modelAndView.model.entities[0].equals(target)
		dashboardTargetController.modelAndView.model.entityCount == 1
	}
}
