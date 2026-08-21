package org.openmrs.module.labtestreport;

import java.util.Date;

public class StockBatchExpiryRow {

	private Integer stockItemId;

	private String itemName;

	private Integer locationId;

	private String locationName;

	private String batchNo;

	private Date expirationDate;

	private double remainingQty;

	private Integer daysUntilExpiry;

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

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public double getRemainingQty() {
		return remainingQty;
	}

	public void setRemainingQty(double remainingQty) {
		this.remainingQty = remainingQty;
	}

	public Integer getDaysUntilExpiry() {
		return daysUntilExpiry;
	}

	public void setDaysUntilExpiry(Integer daysUntilExpiry) {
		this.daysUntilExpiry = daysUntilExpiry;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}
