package org.openmrs.module.stockmanagement.api.dto;

import java.util.Date;

public class StockBatchDTO {
	
	private Integer id;
	
	private String batchNo;
	
	private Date expiration;
	
	private String uuid;
	
	private String stockItemUuid;
	
	private boolean voided;
	
	private Date expiryNotificationDate;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getBatchNo() {
		return batchNo;
	}
	
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
	public Date getExpiration() {
		return expiration;
	}
	
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getStockItemUuid() {
		return stockItemUuid;
	}
	
	public void setStockItemUuid(String stockItemUuid) {
		this.stockItemUuid = stockItemUuid;
	}
	
	public boolean isVoided() {
		return voided;
	}
	
	public void setVoided(boolean voided) {
		this.voided = voided;
	}
	
	public Date getExpiryNotificationDate() {
		return expiryNotificationDate;
	}
	
	public void setExpiryNotificationDate(Date expiryNotificationDate) {
		this.expiryNotificationDate = expiryNotificationDate;
	}
}
