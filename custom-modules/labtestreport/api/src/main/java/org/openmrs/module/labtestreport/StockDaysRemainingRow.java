package org.openmrs.module.labtestreport;

public class StockDaysRemainingRow {

	private Integer stockItemId;

	private String itemName;

	private Integer locationId;

	private String locationName;

	private double onHandQty;

	private double avgDailyConsumption;

	private Double daysRemaining;

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

	public double getOnHandQty() {
		return onHandQty;
	}

	public void setOnHandQty(double onHandQty) {
		this.onHandQty = onHandQty;
	}

	public double getAvgDailyConsumption() {
		return avgDailyConsumption;
	}

	public void setAvgDailyConsumption(double avgDailyConsumption) {
		this.avgDailyConsumption = avgDailyConsumption;
	}

	/**
	 * Null when there has been no recorded consumption in the averaging window, meaning the
	 * days-remaining estimate is undefined (not zero, not infinite - simply unknown).
	 */
	public Double getDaysRemaining() {
		return daysRemaining;
	}

	public void setDaysRemaining(Double daysRemaining) {
		this.daysRemaining = daysRemaining;
	}
}
