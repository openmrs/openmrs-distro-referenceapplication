package org.openmrs.module.labtestreport.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.labtestreport.CmamFollowUpService;
import org.openmrs.module.labtestreport.CmamSummaryRow;
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
 * Renders the CMAM Follow-up summary report: for each child's most recent CMAM Follow-up
 * encounter in the selected date range, how many fall into each Current Diagnosis / Child Last
 * Status / Alert Status category. Each count links to a drill-down listing the children behind it.
 */
@Controller
@RequestMapping("/module/labtestreport/cmamSummary.form")
public class CmamSummaryReportController {

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Date.class,
		    new CustomDateEditor(new SimpleDateFormat(SummaryReportController.DATE_FORMAT), true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showReport(@RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) {
		List<CmamSummaryRow> rows = Context.getService(CmamFollowUpService.class).getSummaryReport(startDate,
		    endDate);

		Map<String, List<CmamSummaryRow>> byDimension = new LinkedHashMap<>();
		byDimension.put("currentDiagnosis", new ArrayList<>());
		byDimension.put("childLastStatus", new ArrayList<>());
		byDimension.put("alertStatus", new ArrayList<>());
		for (CmamSummaryRow row : rows) {
			byDimension.computeIfAbsent(row.getDimension(), k -> new ArrayList<>()).add(row);
		}

		Map<String, String> dimensionConceptUuids = new LinkedHashMap<>();
		dimensionConceptUuids.put("currentDiagnosis", "51d873b5-3394-4780-87d3-5bfaf5cf0eb8");
		dimensionConceptUuids.put("childLastStatus", "524fea02-d6e8-47c0-84ee-e7b889f08d4c");
		dimensionConceptUuids.put("alertStatus", "47266119-f616-4e8a-b094-518b4c2d660b");

		SimpleDateFormat dateFormat = new SimpleDateFormat(SummaryReportController.DATE_FORMAT);
		ModelMap model = new ModelMap();
		model.addAttribute("currentDiagnosisRows", byDimension.get("currentDiagnosis"));
		model.addAttribute("childLastStatusRows", byDimension.get("childLastStatus"));
		model.addAttribute("alertStatusRows", byDimension.get("alertStatus"));
		model.addAttribute("dimensionConceptUuids", dimensionConceptUuids);
		model.addAttribute("startDate", startDate == null ? "" : dateFormat.format(startDate));
		model.addAttribute("endDate", endDate == null ? "" : dateFormat.format(endDate));
		return new ModelAndView("/module/labtestreport/cmamSummaryReport", model);
	}
}
