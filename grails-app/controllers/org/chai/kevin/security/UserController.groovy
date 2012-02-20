package org.chai.kevin.security

import org.chai.kevin.AbstractEntityController
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class UserController extends AbstractEntityController {

	def getEntity(def id) {
		return User.get(id)
	}

	def createEntity() {
		return new User()
	}

	def getLabel() {
		return 'user.label'
	}
	
	def getTemplate() {
		return "/entity/user/createUser"
	}

	def getModel(def entity) {
		[user:entity, roles: Role.list()]
	}

	def bindParams(def entity) {
		entity.properties = params
		
		if (params.passwordHash!=null && params.confirmPass.equals(params.passwordHash))
			entity.passwordHash = params.passwordHash
	}
	
	def list = {
		adaptParamsForList()
		
		List<User> users = User.list(params);

		render (view: '/entity/list', model:[
			template:"user/userList",
			entities: users,
			entityCount: User.count(),
			code: getLabel()
		])
	}
		
}
