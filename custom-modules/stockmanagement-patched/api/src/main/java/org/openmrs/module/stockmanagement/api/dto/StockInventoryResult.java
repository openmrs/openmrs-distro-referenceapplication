package org.openmrs.module.stockmanagement.api.dto;

import java.util.List;

public class StockInventoryResult extends Result<StockItemInventory> {
	
	List<StockItemInventory> totals;
	
	public StockInventoryResult() {
	}
	
	public StockInventoryResult(List<StockItemInventory> data, long totalRecordCount) {
		super(data, totalRecordCount);
	}
	
	public List<StockItemInventory> getTotals() {
		return totals;
	}
	
	public void setTotals(List<StockItemInventory> totals) {
		this.totals = totals;
	}
}
