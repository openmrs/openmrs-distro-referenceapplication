package org.openmrs.module.labtestreport.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.labtestreport.DiseaseSummaryRow;
import org.openmrs.module.labtestreport.DiseaseSummaryService;
import org.openmrs.module.labtestreport.LabTestReportService;
import org.openmrs.module.labtestreport.PatientEncounterReportService;
import org.openmrs.module.labtestreport.PatientEncounterSummaryRow;
import org.openmrs.module.labtestreport.PatientRow;
import org.openmrs.module.labtestreport.SessionAttendanceRow;
import org.openmrs.module.labtestreport.SessionAttendanceService;
import org.openmrs.module.labtestreport.StockLedgerRow;
import org.openmrs.module.labtestreport.StockLedgerService;
import org.openmrs.module.labtestreport.SummaryRow;
import org.openmrs.module.labtestreport.web.SummaryRowGrouping;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JSON equivalents of the JSP-rendered reports, for the O3 frontend module to consume directly
 * (it needs the internal concept-id/patient-uuid columns the O3 Reports dashboard's generic grid
 * deliberately hides). Serializes with our own bundled Jackson {@link ObjectMapper} rather than
 * relying on whatever HTTP message converters happen to be registered elsewhere in the running
 * webapp, so this doesn't depend on other modules being present.
 */
@Controller
@RequestMapping("/module/labtestreport/api")
public class LabTestReportRestController {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	static {
		OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(SummaryReportController.DATE_FORMAT));
	}

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Date.class,
		    new CustomDateEditor(new SimpleDateFormat(SummaryReportController.DATE_FORMAT), true));
	}

	@RequestMapping(value = "/summary.json", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> summary(@RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) throws JsonProcessingException {
		List<SummaryRow> rows = Context.getService(LabTestReportService.class).getSummaryReport(startDate, endDate);
		SummaryRowGrouping.applyCategoryRowSpans(rows);
		return jsonResponse(rows);
	}

	@RequestMapping(value = "/drilldown.json", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> drilldown(@RequestParam("testConceptId") Integer testConceptId,
	        @RequestParam(value = "gender", required = false) String gender,
	        @RequestParam(value = "ageGroup", required = false) String ageGroup,
	        @RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) throws JsonProcessingException {
		List<PatientRow> rows = Context.getService(LabTestReportService.class).getPatientsForCell(testConceptId, gender,
		    ageGroup, startDate, endDate);
		return jsonResponse(rows);
	}

	@RequestMapping(value = "/encounters.json", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> encounters(@RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) throws JsonProcessingException {
		List<PatientEncounterSummaryRow> rows = Context.getService(PatientEncounterReportService.class)
		        .getPatientEncounterSummary(startDate, endDate);
		return jsonResponse(rows);
	}

	@RequestMapping(value = "/disease-summary.json", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> diseaseSummary(@RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) throws JsonProcessingException {
		List<DiseaseSummaryRow> rows = Context.getService(DiseaseSummaryService.class).getSummaryReport(startDate,
		    endDate);
		SummaryRowGrouping.applyDiseaseCategoryRowSpans(rows);
		return jsonResponse(rows);
	}

	@RequestMapping(value = "/disease-drilldown.json", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> diseaseDrilldown(@RequestParam("diagnosisConceptId") Integer diagnosisConceptId,
	        @RequestParam(value = "gender", required = false) String gender,
	        @RequestParam(value = "ageGroup", required = false) String ageGroup,
	        @RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) throws JsonProcessingException {
		List<PatientRow> rows = Context.getService(DiseaseSummaryService.class).getPatientsForCell(diagnosisConceptId,
		    gender, ageGroup, startDate, endDate);
		return jsonResponse(rows);
	}

	@RequestMapping(value = "/session-attendance.json", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> sessionAttendance(@RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) throws JsonProcessingException {
		List<SessionAttendanceRow> rows = Context.getService(SessionAttendanceService.class).getSummaryReport(startDate,
		    endDate);
		return jsonResponse(rows);
	}

	@RequestMapping(value = "/session-attendance-drilldown.json", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> sessionAttendanceDrilldown(@RequestParam("sessionDate") Date sessionDate,
	        @RequestParam("sessionType") String sessionType,
	        @RequestParam(value = "gender", required = false) String gender,
	        @RequestParam(value = "ageGroup", required = false) String ageGroup) throws JsonProcessingException {
		List<PatientRow> rows = Context.getService(SessionAttendanceService.class).getPatientsForCell(sessionDate,
		    sessionType, gender, ageGroup);
		return jsonResponse(rows);
	}

	@RequestMapping(value = "/stock-ledger.json", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> stockLedger(@RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) throws JsonProcessingException {
		List<StockLedgerRow> rows = Context.getService(StockLedgerService.class).getLedgerReport(startDate, endDate);
		return jsonResponse(rows);
	}

	private static <T> ResponseEntity<String> jsonResponse(T body) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(OBJECT_MAPPER.writeValueAsString(body), headers, HttpStatus.OK);
	}
}
