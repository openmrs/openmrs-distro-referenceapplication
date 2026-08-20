package org.openmrs.module.labtestreport.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.api.context.Context;
import org.openmrs.module.labtestreport.StockLedgerRow;
import org.openmrs.module.labtestreport.StockLedgerService;
import org.openmrs.module.labtestreport.web.StockLedgerDayBlock;
import org.openmrs.module.labtestreport.web.StockLedgerGrouping;
import org.openmrs.module.labtestreport.web.StockLedgerItem;
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
 * Renders the stock inventory ledger report as a pivot table: one row per day, one column-group
 * (Actual/Incoming/Outgoing/Remaining) per stock item that had any activity in the selected range.
 */
@Controller
@RequestMapping("/module/labtestreport/stockLedger.form")
public class StockLedgerReportController {

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	@InitBinder
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(DATE_FORMAT), true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showReport(@RequestParam(value = "startDate", required = false) Date startDate,
	        @RequestParam(value = "endDate", required = false) Date endDate) {
		List<StockLedgerRow> rows = Context.getService(StockLedgerService.class).getLedgerReport(startDate, endDate, null);
		List<StockLedgerItem> items = StockLedgerGrouping.buildItemList(rows);
		List<StockLedgerDayBlock> dayBlocks = StockLedgerGrouping.buildDayBlocks(rows, items);

		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		ModelMap model = new ModelMap();
		model.addAttribute("items", items);
		model.addAttribute("dayBlocks", dayBlocks);
		model.addAttribute("totalColumns", 1 + items.size() * 4);
		model.addAttribute("startDate", startDate == null ? "" : dateFormat.format(startDate));
		model.addAttribute("endDate", endDate == null ? "" : dateFormat.format(endDate));
		return new ModelAndView("/module/labtestreport/stockLedgerReport", model);
	}
}
