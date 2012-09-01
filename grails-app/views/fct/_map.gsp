<li>
	<g:render template="/templates/reportTitle" model="[program: currentProgram, title: i18n(field:currentProgram.names), file: 'star_small.png']" />
	<g:reportView linkParams="${params}"/>
	
	<div class="selector">
		<g:reportTargetFilter linkParams="${params}" />
		<g:reportValueFilter linkParams="${params}"/>
	</div>
	
	<g:render template="/maps/legend" model="[indicators: fctTable.targetOptions]"/>
	<g:render template="/maps/colors"/>
	<g:render template="/fct/reportProgramMap" 
		model="[linkParams:params, reportTable: fctTable, reportLocations: fctTable.locations, reportIndicators: fctTable.targetOptions]"/>
	<g:render template="/fct/reportProgramMapTable" 
		model="[linkParams:params, reportTable: fctTable, reportLocations: fctTable.locations, reportIndicators: fctTable.targetOptions]"/>
</li>