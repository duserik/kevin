package org.chai.kevin

class ConstantSpec extends GebTests {

	def setup() {
		Initializer.createDummyStructure();
		Initializer.createDataElementsAndExpressions();
	}
	

	def "edit constant works"() {
		when:
			browser.to(ConstantPage)
			editConstant("Constant")
		
		then:
			browser.at(ConstantPage)
			createConstant.entityFormContainer.displayed
		
	}

	def "add constant works"() {
		when:
			browser.to(ConstantPage)
			addConstant()
			
		then:
			browser.at(ConstantPage)
			createConstant.entityFormContainer.displayed
	}
	
	def "cancel new constant"() {
		when:
			browser.to(ConstantPage)
			addConstant()
			createConstant.cancel()
		
		then:
			browser.at(ConstantPage)
			!createConstant.entityFormContainer.displayed
	}
	
	def "save new empty constant displays error"() {
		when:
			browser.to(ConstantPage)
			addConstant()
			createConstant.save()
		
		then:
			browser.at(ConstantPage)
			createConstant.entityFormContainer.displayed
			createConstant.hasError(createConstant.nameField)
			createConstant.hasError(createConstant.shortNameField)
			createConstant.hasError(createConstant.valueField)
	}
	
	def "save new constant displays it on page"() {
		when:
			browser.to(ConstantPage)
			addConstant()
			createConstant.nameField.value("Test Constant")
			createConstant.shortNameField.value("TESTCONST")
			createConstant.valueField.value("100")
			createConstant.save()
		
		then:
			browser.at(ConstantPage)
			constants.displayed
			hasConstant("Test Constant")
	}
	
	
}
