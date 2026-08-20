package org.openmrs.module.labtestreport;

/**
 * One row of a stock-by-location breakdown: a single stock item's total quantity moved
 * at a single location over the report's date range. Shared by the stock consumption
 * (quantity issued out of a location) and stock distribution (quantity transferred into
 * a location from a source) reports, since both are the same shape - just a different
 * operation type and location role feeding the query.
 */
public class StockLocationQtyRow {

	private Integer stockItemId;

	private String itemName;

	private Integer locationId;

	private String locationName;

	private double quantity;

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

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
}
