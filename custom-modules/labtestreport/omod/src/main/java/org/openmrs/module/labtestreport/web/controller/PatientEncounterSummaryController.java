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
 * Renders the patient encounter summary report: one row per patient with at least one matching
 * encounter, showing their current age, encounter count and most recent encounter date. Clicking
 * a row goes to that patient's O3 chart.
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
			String mostRecent = r.getMostRecentEncounterDate() == null ? "" : dateFormat.format(r.getMostRecentEncounterDate());
			rows.add(new Row(r.getPatientUuid(), name, r.getAge(), r.getEncounterCount(), mostRecent));
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

		private final long encounterCount;

		private final String mostRecentEncounterDate;

		public Row(String patientUuid, String name, Integer age, long encounterCount, String mostRecentEncounterDate) {
			this.patientUuid = patientUuid;
			this.name = name;
			this.age = age;
			this.encounterCount = encounterCount;
			this.mostRecentEncounterDate = mostRecentEncounterDate;
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

		public long getEncounterCount() {
			return encounterCount;
		}

		public String getMostRecentEncounterDate() {
			return mostRecentEncounterDate;
		}
	}
}
