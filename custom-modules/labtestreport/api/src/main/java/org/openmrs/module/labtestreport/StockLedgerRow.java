package org.openmrs.module.labtestreport;

import java.util.Date;

/**
 * One row of the stock inventory ledger report: a single stock item's activity at a single
 * location on a single day it actually had a transaction. {@link #actualQty} (opening balance for
 * the day) is derived as {@code remainingQty - incomingQty + outgoingQty}. Days a given
 * item/location had no activity at all are not represented here - the web layer densifies this
 * sparse list into a full item x location x day grid, carrying the last known balance forward
 * across gaps.
 */
public class StockLedgerRow {

	private Integer stockItemId;

	private String itemName;

	private Integer locationId;

	private String locationName;

	private Date ledgerDate;

	private double actualQty;

	private double incomingQty;

	private double outgoingQty;

	private double remainingQty;

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

	public Date getLedgerDate() {
		return ledgerDate;
	}

	public void setLedgerDate(Date ledgerDate) {
		this.ledgerDate = ledgerDate;
	}

	public double getActualQty() {
		return actualQty;
	}

	public void setActualQty(double actualQty) {
		this.actualQty = actualQty;
	}

	public double getIncomingQty() {
		return incomingQty;
	}

	public void setIncomingQty(double incomingQty) {
		this.incomingQty = incomingQty;
	}

	public double getOutgoingQty() {
		return outgoingQty;
	}

	public void setOutgoingQty(double outgoingQty) {
		this.outgoingQty = outgoingQty;
	}

	public double getRemainingQty() {
		return remainingQty;
	}

	public void setRemainingQty(double remainingQty) {
		this.remainingQty = remainingQty;
	}
}
