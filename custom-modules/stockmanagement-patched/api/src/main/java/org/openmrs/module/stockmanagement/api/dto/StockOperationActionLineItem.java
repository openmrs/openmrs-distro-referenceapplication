package org.openmrs.module.stockmanagement.api.dto;

import java.math.BigDecimal;

public class StockOperationActionLineItem {
	
	private String uuid;
	
	private BigDecimal amount;
	
	private String packagingUoMUuId;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getPackagingUoMUuId() {
		return packagingUoMUuId;
	}
	
	public void setPackagingUoMUuId(String packagingUoMUuId) {
		this.packagingUoMUuId = packagingUoMUuId;
	}
}
