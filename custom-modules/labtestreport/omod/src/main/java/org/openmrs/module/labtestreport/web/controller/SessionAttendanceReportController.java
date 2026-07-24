package org.openmrs.module.labtestreport.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.labtestreport.SessionAttendanceRow;
import org.openmrs.module.labtestreport.SessionAttendanceService;
import org.openmrs.module.labtestreport.web.SessionAttendanceDayBlock;
import org.openmrs.module.labtestreport.web.SessionAttendanceGrouping;
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
 * Renders the session attendance report as repeating per-day blocks (Individual Sessions, Group
 * Sessions, Case follow up, then a "Total of the day" row), matching the CARE facility template
 * this report replicates.
 */
@Controller
@RequestMapping("/module/labtestreport/sessionAttendance.form")
public class SessionAttendanceReportController {

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	private static final String[][] AGE_GENDER_COLUMNS = { { "0-4", "M" }, { "0-4", "F" }, { "5-14", "M" },
	        { "5-14", "F" }, { "15-18", "M" }, { "15-18", "F" }, { "19-49", "M" }, { "19-49", "F" }, { "50-65", "M" },
	        { "50-65", "F" }, { "65+", "M" }, { "65+", "F" } };

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(DATE_FORMAT), true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showReport(@RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) {
		List<SessionAttendanceRow> rows = Context.getService(SessionAttendanceService.class)
		        .getSummaryReport(startDate, endDate);
		List<SessionAttendanceDayBlock> dayBlocks = SessionAttendanceGrouping.buildDayBlocks(rows);

		List<SummaryReportController.Column> columns = new ArrayList<>();
		for (String[] ageGenderColumn : AGE_GENDER_COLUMNS) {
			columns.add(new SummaryReportController.Column(ageGenderColumn[0], ageGenderColumn[1]));
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		ModelMap model = new ModelMap();
		model.addAttribute("dayBlocks", dayBlocks);
		model.addAttribute("columns", columns);
		model.addAttribute("startDate", startDate == null ? "" : dateFormat.format(startDate));
		model.addAttribute("endDate", endDate == null ? "" : dateFormat.format(endDate));
		return new ModelAndView("/module/labtestreport/sessionAttendanceReport", model);
	}
}
