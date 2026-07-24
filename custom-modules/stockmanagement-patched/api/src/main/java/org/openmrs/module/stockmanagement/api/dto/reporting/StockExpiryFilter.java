package org.openmrs.module.stockmanagement.api.dto.reporting;

public class StockExpiryFilter extends ReportFilter {
	
	private Integer stockBatchIdMin;
	
	private Integer stockItemIdMin;
	
	public Integer getStockBatchIdMin() {
		return stockBatchIdMin;
	}
	
	public void setStockBatchIdMin(Integer stockBatchIdMin) {
		this.stockBatchIdMin = stockBatchIdMin;
	}
	
	public Integer getStockItemIdMin() {
		return stockItemIdMin;
	}
	
	public void setStockItemIdMin(Integer stockItemIdMin) {
		this.stockItemIdMin = stockItemIdMin;
	}
}
