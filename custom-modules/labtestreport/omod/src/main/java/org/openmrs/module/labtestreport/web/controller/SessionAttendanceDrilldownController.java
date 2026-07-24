package org.openmrs.module.labtestreport.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.labtestreport.PatientRow;
import org.openmrs.module.labtestreport.SessionAttendanceService;
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
 * Drills down from a single session attendance report cell to the patients behind its count.
 * Reuses the same patientDrilldown.jsp view as the lab-test and disease drilldowns.
 */
@Controller
@RequestMapping("/module/labtestreport/sessionAttendanceDrilldown.form")
public class SessionAttendanceDrilldownController {

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Date.class,
		    new CustomDateEditor(new SimpleDateFormat(SessionAttendanceReportController.DATE_FORMAT), true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showPatients(@RequestParam("sessionDate") Date sessionDate,
	        @RequestParam("sessionType") String sessionType,
	        @RequestParam(value = "gender", required = false) String gender,
	        @RequestParam(value = "ageGroup", required = false) String ageGroup,
	        @RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) {
		List<PatientRow> patients = Context.getService(SessionAttendanceService.class).getPatientsForCell(sessionDate,
		    sessionType, gender, ageGroup);

		SimpleDateFormat dateFormat = new SimpleDateFormat(SessionAttendanceReportController.DATE_FORMAT);
		ModelMap model = new ModelMap();
		model.addAttribute("patients", patients);
		model.addAttribute("category", dateFormat.format(sessionDate));
		model.addAttribute("labTest", sessionType);
		model.addAttribute("ageGroup", ageGroup);
		model.addAttribute("gender", gender);
		model.addAttribute("startDate", startDate == null ? "" : dateFormat.format(startDate));
		model.addAttribute("endDate", endDate == null ? "" : dateFormat.format(endDate));
		model.addAttribute("backUrl", "/module/labtestreport/sessionAttendance.form");
		return new ModelAndView("/module/labtestreport/patientDrilldown", model);
	}
}
