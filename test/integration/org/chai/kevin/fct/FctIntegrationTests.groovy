package org.chai.kevin.fct

import org.chai.kevin.IntegrationTests;
import org.chai.kevin.data.Type;
import org.chai.kevin.util.Utils;

abstract class FctIntegrationTests extends IntegrationTests {

	def newFctObjective(def code) {
		return new FctObjective(code: code, names: [:]).save(failOnError: true);
	}
	
	def newFctTarget(def code, def sum, def format, def groups, def objective) {
		def target = new FctTarget(names: [:], 
			code: code, 
			format: format, 
			sum: sum, 
			objective: objective, 
			groupUuidString: Utils.unsplit(groups)).save(failOnError: true)
			
		objective.targets << target
		objective.save(failOnError: true)
		return target
	}
		
	def newFctTarget(def code, def expression, def groups, def objective) {
		return newFctTarget(code, expression, null, groups, objective)
	}
}