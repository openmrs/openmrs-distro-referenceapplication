package org.openmrs.module.labtestreport.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.labtestreport.DiseaseSummaryService;
import org.openmrs.module.labtestreport.PatientRow;
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
 * Lists the distinct patients behind a single cell of the disease surveillance summary report.
 * Same shape as {@link PatientDrilldownController}, sourced from encounter_diagnosis.
 */
@Controller
@RequestMapping("/module/labtestreport/diseaseDrilldown.form")
public class DiseaseDrilldownController {

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Date.class,
		    new CustomDateEditor(new SimpleDateFormat(SummaryReportController.DATE_FORMAT), true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showPatients(@RequestParam("diagnosisConceptId") Integer diagnosisConceptId,
	        @RequestParam(value = "gender", required = false) String gender,
	        @RequestParam(value = "ageGroup", required = false) String ageGroup,
	        @RequestParam(value = "category", required = false) String category,
	        @RequestParam(value = "diagnosisLabel", required = false) String diagnosisLabel,
	        @RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) {

		List<PatientRow> patients = Context.getService(DiseaseSummaryService.class).getPatientsForCell(
		    diagnosisConceptId, gender, ageGroup, startDate, endDate);

		SimpleDateFormat dateFormat = new SimpleDateFormat(SummaryReportController.DATE_FORMAT);
		ModelMap model = new ModelMap();
		model.addAttribute("patients", patients);
		model.addAttribute("category", category);
		model.addAttribute("labTest", diagnosisLabel);
		model.addAttribute("ageGroup", ageGroup);
		model.addAttribute("gender", gender);
		model.addAttribute("startDate", startDate == null ? "" : dateFormat.format(startDate));
		model.addAttribute("endDate", endDate == null ? "" : dateFormat.format(endDate));
		model.addAttribute("backUrl", "/module/labtestreport/diseaseSummary.form");
		return new ModelAndView("/module/labtestreport/patientDrilldown", model);
	}
}
