package org.openmrs.module.stockmanagement.api.reporting;

public enum ReportParameter {
	Date(true, false, false, false, false, false, false, false, false, false, false, false), StartDate(true, false, false,
	        false, false, false, false, false, false, false, false, false), EndDate(true, false, false, false, false, false,
	        false, false, false, false, false, false), StockItemCategory(false, true, false, false, false, false, false,
	        false, false, false, false, false), Location(false, false, true, false, false, false, false, false, false,
	        false, false, false), ChildLocations(false, false, false, true, false, false, false, false, false, false, false,
	        false), StockSource(false, false, false, false, true, false, false, false, false, false, false, false), StockSourceDestination(
	        false, false, false, false, true, false, false, false, false, false, false, false), InventoryGroupBy(false,
	        false, false, false, false, true, false, false, false, false, false, false), MaxReorderLevelRatio(false, false,
	        false, false, false, false, true, false, false, false, false, false), Patient(false, false, false, false, false,
	        false, false, true, false, false, false, false), StockItem(false, false, false, false, false, false, false,
	        false, true, false, false, false), MostLeastMoving(false, false, false, false, false, false, false, false,
	        false, true, false, false), Limit(false, false, false, false, false, false, false, false, false, false, true,
	        false), Fullfillment(false, false, false, false, false, false, false, false, false, false, false, true);
	
	private boolean isDate;
	
	private boolean isStockItemCategory;
	
	private boolean isLocation;
	
	private boolean isBoolean;
	
	private boolean isStockSource;
	
	private boolean isInventoryGroupBy;
	
	private boolean isMaxReorderLevelRatio;
	
	private boolean isPatient;
	
	private boolean isStockItem;
	
	private boolean isMostLeastMoving;
	
	private boolean isUint;
	
	private boolean fullfillment;
	
	ReportParameter(boolean isDate, boolean isStockItemCategory, boolean isLocation, boolean isBoolean,
	    boolean isStockSource, boolean isInventoryGroupBy, boolean isMaxReorderLevelRatio, boolean isPatient,
	    boolean isStockItem, boolean isMostLeastMoving, boolean isUint, boolean fullfillment) {
		this.isDate = isDate;
		this.isStockItemCategory = isStockItemCategory;
		this.isLocation = isLocation;
		this.isBoolean = isBoolean;
		this.isStockSource = isStockSource;
		this.isInventoryGroupBy = isInventoryGroupBy;
		this.isMaxReorderLevelRatio = isMaxReorderLevelRatio;
		this.isPatient = isPatient;
		this.isStockItem = isStockItem;
		this.isMostLeastMoving = isMostLeastMoving;
		this.isUint = isUint;
		this.fullfillment = fullfillment;
	}
	
	public static ReportParameter findByName(String name) {
		return findInList(name, values());
	}
	
	public static ReportParameter findInList(String name, ReportParameter[] parameterList) {
		ReportParameter result = null;
		for (ReportParameter enumValue : parameterList) {
			if (enumValue.name().equalsIgnoreCase(name)) {
				result = enumValue;
				break;
			}
		}
		return result;
	}
	
	public boolean isDate() {
		return isDate;
	}
	
	public boolean isStockItemCategory() {
		return isStockItemCategory;
	}
	
	public boolean isLocation() {
		return isLocation;
	}
	
	public boolean isBoolean() {
		return isBoolean;
	}
	
	public boolean isStockSource() {
		return isStockSource;
	}
	
	public boolean isInventoryGroupBy() {
		return isInventoryGroupBy;
	}
	
	public boolean isMaxReorderLevelRatio() {
		return isMaxReorderLevelRatio;
	}
	
	public boolean isPatient() {
		return isPatient;
	}
	
	public boolean isStockItem() {
		return isStockItem;
	}
	
	public boolean isMostLeastMoving() {
		return isMostLeastMoving;
	}
	
	public boolean isUint() {
		return isUint;
	}
	
	public boolean isFullfillment() {
		return fullfillment;
	}
	
}
