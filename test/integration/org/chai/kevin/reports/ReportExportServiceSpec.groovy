package org.chai.kevin.reports

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.chai.kevin.IntegrationTests;
import org.chai.kevin.data.Type;
import org.chai.kevin.dsr.DsrIntegrationTests;
import org.chai.kevin.fct.FctIntegrationTests;
import org.chai.kevin.dsr.DsrTarget;
import org.chai.location.DataLocation;
import org.chai.location.DataLocationType;
import org.chai.location.Location;
import org.chai.location.LocationLevel;
import org.chai.kevin.util.Utils;
import org.chai.kevin.value.RawDataElementValue
import org.chai.kevin.value.Value;
import org.chai.kevin.reports.ReportService.ReportType;

class ReportExportServiceSpec extends ReportIntegrationTests {

	def reportExportService
	def dsrService
	def fctService
	
	def "test for get dsr zip file"(){
		setup:
		setupLocationTree()
		setupProgramTree()
		def period = newPeriod()
		def program = ReportProgram.findByCode(PROGRAM1)
		def location = Location.findByCode(BURERA)
		def dataElement = newRawDataElement(CODE(2), Type.TYPE_NUMBER())
		def dataElementValue1 = newRawDataElementValue(dataElement, period, DataLocation.findByCode(KIVUYE), v("30"))
		def dataElementValue2 = newRawDataElementValue(dataElement, period, DataLocation.findByCode(BUTARO), v("50"))
		def category = DsrIntegrationTests.newDsrTargetCategory(CODE(1), program, 1)
		def target = DsrIntegrationTests.newDsrTarget(CODE(3), ['en': 'Target'], 1, dataElement, category)
		def types = new HashSet([
			DataLocationType.findByCode(DISTRICT_HOSPITAL_GROUP),
			DataLocationType.findByCode(HEALTH_CENTER_GROUP)
		])
		def reportType = ReportType.TABLE
		refresh()
		
		when:
		def dsrTable = dsrService.getDsrTable(location, period, types, category, reportType)
		
		then:
		dsrTable != null
		dsrTable.hasData() == true
		
		when:
		def csvFile = reportExportService.getReportExportFile("file", dsrTable, location)
		def zipFile = Utils.getZipFile(csvFile, "file")
		
		then:
		zipFile.exists() == true
		zipFile.length() > 0
		
		when:
		def lines = IntegrationTests.readLines(csvFile)
		  
		then:
		lines[0] == "Location,Locations,Target"
		lines[1] == "Burera,Rwanda-North,N/A"
		lines[2] == "Butaro DH,Rwanda-North-Burera,50"
		lines[3] == "Kivuye HC,Rwanda-North-Burera,30"
	}
	
	def "test for get fct zip file"(){
		setup:
		setupLocationTree()
		setupProgramTree()
		def period = newPeriod()
		def normalizedDataElement = newNormalizedDataElement(CODE(1), Type.TYPE_NUMBER(), [(period.id+''):[(DISTRICT_HOSPITAL_GROUP):"1", (HEALTH_CENTER_GROUP):"1"]])
		def program = ReportProgram.findByCode(PROGRAM1)
		def sum = newSum("\$"+normalizedDataElement.id, CODE(2))
		def target = FctIntegrationTests.newFctTarget(CODE(3), 1, program)
		def targetOption = FctIntegrationTests.newFctTargetOption(CODE(4), ['en': 'Option'], 1, target, sum)
		def location = Location.findByCode(NORTH)
		def dataLocationTypes = new HashSet([DataLocationType.findByCode(DISTRICT_HOSPITAL_GROUP), DataLocationType.findByCode(HEALTH_CENTER_GROUP)])
		def reportType = ReportType.TABLE
		def fctTable = null
		refresh()
		
		when:
		fctTable = fctService.getFctTable(location, target, period, dataLocationTypes, reportType)
		
		then:
		fctTable != null
		fctTable.hasData() == true
		
		when:
		def csvFile = reportExportService.getReportExportFile("file", fctTable, location)
		def zipFile = Utils.getZipFile(csvFile, "file")
		
		then:
		zipFile.exists() == true
		zipFile.length() > 0
		
		when:
		def lines = IntegrationTests.readLines(csvFile)
		  
		then:
		lines[0] == "Location,Locations,Option"
		lines[1] == "North,Rwanda,2"
		lines[2] == "Burera,Rwanda-North,2"
		lines[3] == "Butaro DH,Rwanda-North-Burera,1"
		lines[4] == "Kivuye HC,Rwanda-North-Burera,1"
		
	}
	
	def "test for valid export filename"() {
		setup:
		setupLocationTree()
		setupProgramTree()
		def period = newPeriod()
		def program = ReportProgram.findByCode(PROGRAM1)
		def location = Location.findByCode(BURERA)
		def dataElement = newRawDataElement(CODE(2), Type.TYPE_NUMBER())
		def category = DsrIntegrationTests.newDsrTargetCategory(CODE(1), program, 1)
		def target = DsrIntegrationTests.newDsrTarget(CODE(3), 1, dataElement, category)
		def types = new HashSet([
			DataLocationType.findByCode(DISTRICT_HOSPITAL_GROUP),
			DataLocationType.findByCode(HEALTH_CENTER_GROUP)
		])
		def reportType = ReportType.TABLE
		
		when:
		def file = reportExportService.getReportExportFilename("ReportsDSR", location, program, period)
		
		then:
		file.startsWith("ReportsDSR_2005_Program1_Burera")
	}

}