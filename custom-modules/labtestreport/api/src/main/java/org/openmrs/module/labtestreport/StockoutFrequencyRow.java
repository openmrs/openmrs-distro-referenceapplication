package org.openmrs.module.labtestreport;

public class StockoutFrequencyRow {

	private Integer stockItemId;

	private String itemName;

	private Integer locationId;

	private String locationName;

	private int stockoutDays;

	private int activeDays;

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

	/**
	 * Count of days (with recorded stock activity) on which the item's end-of-day balance at this
	 * location was zero or below - not full calendar-day coverage, since gaps between recorded
	 * transactions aren't back-filled.
	 */
	public int getStockoutDays() {
		return stockoutDays;
	}

	public void setStockoutDays(int stockoutDays) {
		this.stockoutDays = stockoutDays;
	}

	public int getActiveDays() {
		return activeDays;
	}

	public void setActiveDays(int activeDays) {
		this.activeDays = activeDays;
	}
}
