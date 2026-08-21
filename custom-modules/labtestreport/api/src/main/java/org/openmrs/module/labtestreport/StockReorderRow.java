package org.openmrs.module.labtestreport;

public class StockReorderRow {

	private Integer stockItemId;

	private String itemName;

	private Integer locationId;

	private String locationName;

	private String ruleName;

	private double reorderLevel;

	private double onHandQty;

	private String unitName;

	public Integer getStockItemId() {
		return stockItemId;
	}

	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public double getReorderLevel() {
		return reorderLevel;
	}

	public void setReorderLevel(double reorderLevel) {
		this.reorderLevel = reorderLevel;
	}

	public double getOnHandQty() {
		return onHandQty;
	}

	public void setOnHandQty(double onHandQty) {
		this.onHandQty = onHandQty;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}
