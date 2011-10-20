<table class="listing">
	<thead>
		<tr>
			<th/>
			<th><g:message code="period.startdate.label" default="Start Date" /></th>
			<th><g:message code="period.enddate.label" default="End Date" /></th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${entities}" status="i" var="iteration">
			<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
				<td>
					<ul class="horizontal">
						<li>
							<a class="edit-link" href="${createLinkWithTargetURI(controller:'iteration', action:'edit', params:[id: iteration.id])}">
								<g:message code="default.link.edit.label" default="Edit" />
							</a>
						</li>
						<li>
							<a class="delete-link" href="${createLinkWithTargetURI(controller:'iteration', action:'delete', params:[id: iteration.id])}" onclick="return confirm('\${message(code: 'default.link.delete.confirm.message', default: 'Are you sure?')}');"><g:message code="default.link.delete.label" default="Delete" /></a>
						</li>
						
					</ul>
				</td>
				<td>${iteration.startDate}</td>
				<td>${iteration.endDate}</td>
			</tr>
		</g:each>
	</tbody>
</table>
