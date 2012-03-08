<table class="listing">
	<thead>
		<tr>
			<th/>
			<th><g:message code="dataEntityType.name.label" default="Name"/></th>
			<th>Code</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${entities}" status="i" var="dataEntityType">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				<td>
					<ul class="horizontal">
						<li>
							<a class="edit-link" href="${createLinkWithTargetURI(controller:'dataEntityType', action:'edit', params:[id: dataEntityType.id])}">
								<g:message code="default.link.edit.label" default="Edit" />
							</a>
						</li>
						<li>
							<a class="delete-link" href="${createLinkWithTargetURI(controller:'dataEntityType', action:'delete', params:[id: dataEntityType.id])}" onclick="return confirm('\${message(code: 'default.link.delete.confirm.message', default: 'Are you sure?')}');"><g:message code="default.link.delete.label" default="Delete" /></a>
						</li>
						
					</ul>
				</td>
				<td>
					<g:i18n field="${dataEntityType.names}"/>
				</td>
				<td>${dataEntityType.code}</td>
			</tr>
		</g:each>
	</tbody>
</table>