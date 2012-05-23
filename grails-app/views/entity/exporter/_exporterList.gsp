<%@page import="org.chai.kevin.util.Utils"%>
<div class="table-wrap">
	<table class="listing">
		<thead>
			<tr>
				<th/>
				<th>Descriptions</th>
				<th>Locations</th>
				<th>Periods</th>
				<th>Data Location Types</th>
				<th>Data Elements</th>
				<th>Created On</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<g:each in="${entities}" status="i" var="export">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td>
	            		<ul class="horizontal">
			           		<li>
			           			<a class="edit-link" href="${createLinkWithTargetURI(controller:exporter, action:'edit', params:[id: export.id])}"><g:message code="default.link.edit.label" /></a>
							</li>
			           		<li>
			           			<a class="delete-link" href="${createLinkWithTargetURI(controller:'exporter', action:'delete', params:[id:export.id])}" onclick="return confirm('\${message(code: 'default.link.delete.confirm.message')}');"><g:message code="default.link.delete.label" /></a>
							</li>
		           		</ul>
					</td>
					<td><g:i18n field="${export.descriptions}"/></td>
					<td>
						<g:each in="${export.locations}" status="l" var="location">
							<g:i18n field="${location.parent?.names}"/> - <g:i18n field="${location.names}"/>
							${(l < export.locations.size()-1)? ',' : ''}
						</g:each>
					</td>
	  				<td>
		  				<g:each in="${export.periods}" status="p" var="period">
							[${Utils.formatDate(period.startDate)} - ${Utils.formatDate(period.endDate)}]
							${(p < export.periods.size()-1)? ',' : ''}
						</g:each>
	  				</td>
	  				<td>
	  					${export.typeCodeString}
	  				</td>
	  				<td>
		  				<g:each in="${export.data}" status="d" var="data">
		  				 	<g:i18n field="${data.names}"/>
							${(d < export.data.size()-1)? ',' : ''}
						</g:each>

	  				</td>
	  				<td>${Utils.formatDateWithTime(export.date)}</td>
	  				<td>
  						<a href="${createLinkWithTargetURI(controller:'exporter', action:'export', params:['export.id': export.id, method: method])}">Download</a>
	  				</td>
				</tr>
			</g:each>
		</tbody>
	</table>
</div>