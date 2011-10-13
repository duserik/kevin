<div class="entity-list">
	<div id="entities">
		<h5 class="float-left">
           	<g:message code="default.list.label" args="[entityName]" />
		</h5>
		
		<g:if test="${!search}">
			<div class="float-right">
				<g:if test="${!addTemplate}">
					<a href="${createLinkWithTargetURI(controller: params['controller'], action:'create', params: params)}" >New ${entityName}</a>
				</g:if>
				<g:else>
					<g:render template="/survey/admin/${addTemplate}"/>
				</g:else>
			</div>
		</g:if>
		<div class="clear"></div>

		<g:if test="${flash.message}">
			<div class="rounded-box-top rounded-box-bottom flash-info">
           		${flash.message}
           	</div>
        </g:if>
			
		<!-- Template goes here -->
		<g:if test="${grailsApplication.getArtefactByLogicalPropertyName('Controller', controllerName).hasProperty('search')}">
			<g:searchBox action="search"/>
		</g:if>
		<g:if test="${!entities.isEmpty()}">
			<g:render template="${template}"/>
			
			<!-- End of template -->
			<div class="paginateButtons">
				<g:paginate total="${entityCount}" params="${params}" action="${actionName}"/>
			</div>
		</g:if>
		<g:else>
			<div>No ${entityName} available</div>
		</g:else>				
	</div>
</div>
