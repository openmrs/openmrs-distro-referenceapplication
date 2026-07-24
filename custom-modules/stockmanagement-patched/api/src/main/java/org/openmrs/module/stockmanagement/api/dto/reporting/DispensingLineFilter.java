package org.openmrs.module.stockmanagement.api.dto.reporting;

public class DispensingLineFilter extends ReportFilter {
	
	private Integer stockItemTransactionMin;
	
	private Integer patientId;
	
	private Integer stockItemId;
	
	public Integer getStockItemTransactionMin() {
		return stockItemTransactionMin;
	}
	
	public void setStockItemTransactionMin(Integer stockItemTransactionMin) {
		this.stockItemTransactionMin = stockItemTransactionMin;
	}
	
	public Integer getPatientId() {
		return patientId;
	}
	
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	public Integer getStockItemId() {
		return stockItemId;
	}
	
	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}
}
