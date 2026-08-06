package org.openmrs.module.labtestreport.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.labtestreport.CmamFollowUpService;
import org.openmrs.module.labtestreport.CmamPatientRow;
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
 * Lists the children behind a single cell of the CMAM Follow-up summary report -- those whose
 * most recent CMAM encounter in the selected date range has the given category for the given
 * dimension (Current Diagnosis / Child Last Status / Alert Status).
 */
@Controller
@RequestMapping("/module/labtestreport/cmamDrilldown.form")
public class CmamDrilldownController {

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Date.class,
		    new CustomDateEditor(new SimpleDateFormat(SummaryReportController.DATE_FORMAT), true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showPatients(@RequestParam("dimensionConceptUuid") String dimensionConceptUuid,
	        @RequestParam("categoryConceptId") Integer categoryConceptId,
	        @RequestParam(value = "dimensionLabel", required = false) String dimensionLabel,
	        @RequestParam(value = "categoryLabel", required = false) String categoryLabel,
	        @RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) {

		List<CmamPatientRow> patients = Context.getService(CmamFollowUpService.class).getPatientsForCategory(
		    dimensionConceptUuid, categoryConceptId, startDate, endDate);

		SimpleDateFormat dateFormat = new SimpleDateFormat(SummaryReportController.DATE_FORMAT);
		ModelMap model = new ModelMap();
		model.addAttribute("patients", patients);
		model.addAttribute("dimensionLabel", dimensionLabel);
		model.addAttribute("categoryLabel", categoryLabel);
		model.addAttribute("startDate", startDate == null ? "" : dateFormat.format(startDate));
		model.addAttribute("endDate", endDate == null ? "" : dateFormat.format(endDate));
		model.addAttribute("backUrl", "/module/labtestreport/cmamSummary.form");
		return new ModelAndView("/module/labtestreport/cmamDrilldown", model);
	}
}
