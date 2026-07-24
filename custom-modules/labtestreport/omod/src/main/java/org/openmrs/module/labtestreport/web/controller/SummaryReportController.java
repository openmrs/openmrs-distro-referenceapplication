package org.openmrs.module.labtestreport.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.labtestreport.LabTestReportService;
import org.openmrs.module.labtestreport.SummaryRow;
import org.openmrs.module.labtestreport.web.SummaryRowGrouping;
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
 * Renders the lab test summary report: one row per category/lab-test combination, with a
 * merged category cell spanning every lab test in that category. {@code startDate}/{@code endDate}
 * are optional and, when supplied, are also forwarded to every drill-down link so the patient
 * list stays consistent with the filtered summary.
 */
@Controller
@RequestMapping("/module/labtestreport/summary.form")
public class SummaryReportController {

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	private static final String[][] AGE_GENDER_COLUMNS = { { "0-4", "M" }, { "0-4", "F" }, { "5-14", "M" },
	        { "5-14", "F" }, { "15-18", "M" }, { "15-18", "F" }, { "19-49", "M" }, { "19-49", "F" }, { "50-65", "M" },
	        { "50-65", "F" }, { "65+", "M" }, { "65+", "F" } };

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(DATE_FORMAT), true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showReport(
	        @RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) {
		List<SummaryRow> rows = Context.getService(LabTestReportService.class).getSummaryReport(startDate, endDate);
		SummaryRowGrouping.applyCategoryRowSpans(rows);

		List<Column> columns = new ArrayList<>();
		for (String[] ageGenderColumn : AGE_GENDER_COLUMNS) {
			columns.add(new Column(ageGenderColumn[0], ageGenderColumn[1]));
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		ModelMap model = new ModelMap();
		model.addAttribute("rows", rows);
		model.addAttribute("columns", columns);
		model.addAttribute("startDate", startDate == null ? "" : dateFormat.format(startDate));
		model.addAttribute("endDate", endDate == null ? "" : dateFormat.format(endDate));
		return new ModelAndView("/module/labtestreport/summaryReport", model);
	}

	public static class Column {

		private final String ageGroup;

		private final String gender;

		public Column(String ageGroup, String gender) {
			this.ageGroup = ageGroup;
			this.gender = gender;
		}

		public String getAgeGroup() {
			return ageGroup;
		}

		public String getGender() {
			return gender;
		}

		public String getKey() {
			return ageGroup + "_" + gender;
		}
	}
}
