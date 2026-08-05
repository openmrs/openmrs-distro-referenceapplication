package org.openmrs.module.labtestreport.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.labtestreport.PatientEncounterReportService;
import org.openmrs.module.labtestreport.PatientEncounterSummaryRow;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Renders the patient visit summary report: one row per patient with at least one matching
 * visit, showing their current age, visit count and most recent visit date. Clicking a row goes
 * to that patient's O3 chart.
 */
@Controller
@RequestMapping("/module/labtestreport/patientEncounters.form")
public class PatientEncounterSummaryController {

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Date.class,
		    new CustomDateEditor(new SimpleDateFormat(SummaryReportController.DATE_FORMAT), true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showReport(@RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) {
		List<PatientEncounterSummaryRow> summaryRows = Context.getService(PatientEncounterReportService.class)
		        .getPatientEncounterSummary(startDate, endDate);

		SimpleDateFormat dateFormat = new SimpleDateFormat(SummaryReportController.DATE_FORMAT);
		List<Row> rows = new ArrayList<>();
		for (PatientEncounterSummaryRow r : summaryRows) {
			String name = (r.getGivenName() + " " + r.getFamilyName()).trim();
			String mostRecent = r.getMostRecentVisitDate() == null ? "" : dateFormat.format(r.getMostRecentVisitDate());
			rows.add(new Row(r.getPatientUuid(), name, r.getAge(), r.getVisitCount(), mostRecent));
		}

		ModelMap model = new ModelMap();
		model.addAttribute("rows", rows);
		model.addAttribute("startDate", startDate == null ? "" : dateFormat.format(startDate));
		model.addAttribute("endDate", endDate == null ? "" : dateFormat.format(endDate));
		return new ModelAndView("/module/labtestreport/patientEncounterSummary", model);
	}

	public static class Row {

		private final String patientUuid;

		private final String name;

		private final Integer age;

		private final long visitCount;

		private final String mostRecentVisitDate;

		public Row(String patientUuid, String name, Integer age, long visitCount, String mostRecentVisitDate) {
			this.patientUuid = patientUuid;
			this.name = name;
			this.age = age;
			this.visitCount = visitCount;
			this.mostRecentVisitDate = mostRecentVisitDate;
		}

		public String getPatientUuid() {
			return patientUuid;
		}

		public String getName() {
			return name;
		}

		public Integer getAge() {
			return age;
		}

		public long getVisitCount() {
			return visitCount;
		}

		public String getMostRecentVisitDate() {
			return mostRecentVisitDate;
		}
	}
}
