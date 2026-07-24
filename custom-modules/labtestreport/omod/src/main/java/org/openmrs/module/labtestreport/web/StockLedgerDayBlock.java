package org.openmrs.module.labtestreport.web;

import java.util.Date;
import java.util.List;

import org.openmrs.module.labtestreport.StockLedgerRow;

/**
 * One day's worth of the stock ledger report: one cell per tracked item (in the same order as the
 * report's item list), densified so every item has a value every day even if it had no activity
 * that day (in which case its balance is simply carried forward unchanged).
 */
public class StockLedgerDayBlock {

	private Date date;

	private List<StockLedgerRow> cells;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<StockLedgerRow> getCells() {
		return cells;
	}

	public void setCells(List<StockLedgerRow> cells) {
		this.cells = cells;
	}
}
