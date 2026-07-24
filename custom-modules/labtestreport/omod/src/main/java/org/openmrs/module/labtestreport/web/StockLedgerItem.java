package org.openmrs.module.labtestreport.web;

public class StockLedgerItem {

	private final Integer stockItemId;

	private final String itemName;

	public StockLedgerItem(Integer stockItemId, String itemName) {
		this.stockItemId = stockItemId;
		this.itemName = itemName;
	}

	public Integer getStockItemId() {
		return stockItemId;
	}

	public String getItemName() {
		return itemName;
	}
}
