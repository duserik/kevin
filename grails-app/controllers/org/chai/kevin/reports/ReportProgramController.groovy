
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
package org.chai.kevin.reports
/**
* @author Jean Kahigiso M.
*
*/
import grails.plugin.springcache.annotations.CacheFlush

import org.chai.kevin.AbstractEntityController
import org.chai.kevin.reports.ReportProgram


class ReportProgramController extends AbstractEntityController{
	
	def dataService;
	
	def getEntity(def id) {
		return ReportProgram.get(id)
	}
	
	def createEntity() {
		return new ReportProgram()
	}
	
	def saveEntity(def entity) {
		super.saveEntity(entity)
		
		// refresh cache
		if (entity.parent != null) entity.parent.addToChildren(entity)
	}
	
	def getLabel() {
		return "reports.program.label"
	}
	
	def getTemplate() {
		return "/entity/reports/createProgram"
	}
	
	def getModel(def entity) {
		[ program: entity, programs: ReportProgram.list() ]
	}
	
	def getEntityClass(){
		return ReportProgram.class;
	}
	
	def bindParams(def entity) {
		entity.properties = params
	}
	
	def search = {
		adaptParamsForList()
		
		def programs = dataService.searchData(ReportProgram.class, params['q'], [], params);
		
		render (view: '/entity/list', model:[
			entities: programs,
			entityCount: programs.totalCount,
			entityClass: getEntityClass(),
			template: "reports/programList",
			code: getLabel(),
			search: true
		])
	}
	
	def list = {
		adaptParamsForList()
		
		List<ReportProgram> programs = ReportProgram.list(params);
		
		render (view: '/entity/list', model:[
			entities: programs,
			template: "reports/programList",
			code: getLabel(),
			entityCount: ReportProgram.count(),
			entityClass: getEntityClass()
		])
	}
}
