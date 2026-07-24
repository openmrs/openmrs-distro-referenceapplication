package org.openmrs.module.stockmanagement.api.model;

public enum LocationType {
	
	/**
	 * The location is from the location table.
	 */
	Location(),
	/**
	 * The location is from the stockmgmt_stock_source table.
	 */
	Other()
}
