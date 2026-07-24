package org.openmrs.module.stockmanagement.api.dto.reporting;

import org.openmrs.module.stockmanagement.api.dto.StockItemInventorySearchFilter;

public class StockInventoryFilter extends ReportFilter {
	
	private StockItemInventorySearchFilter.InventoryGroupBy inventoryGroupBy;
	
	public StockItemInventorySearchFilter.InventoryGroupBy getInventoryGroupBy() {
		return inventoryGroupBy;
	}
	
	public void setInventoryGroupBy(StockItemInventorySearchFilter.InventoryGroupBy inventoryGroupBy) {
		this.inventoryGroupBy = inventoryGroupBy;
	}
}
