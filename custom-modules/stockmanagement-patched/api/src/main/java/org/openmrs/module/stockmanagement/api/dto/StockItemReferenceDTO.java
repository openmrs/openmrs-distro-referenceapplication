package org.openmrs.module.stockmanagement.api.dto;

public class StockItemReferenceDTO {
	
	private Integer id;
	
	private String uuid;
	
	private boolean voided;
	
	private String referenceCode;
	
	private int stockSourceId;
	
	private String stockSourceUuid;
	
	private String stockSourceName;
	
	private int stockItemId;
	
	private String stockItemUuid;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public boolean isVoided() {
		return voided;
	}
	
	public void setVoided(boolean voided) {
		this.voided = voided;
	}
	
	public String getReferenceCode() {
		return referenceCode;
	}
	
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}
	
	public int getStockSourceId() {
		return stockSourceId;
	}
	
	public void setStockSourceId(int stockSourceId) {
		this.stockSourceId = stockSourceId;
	}
	
	public String getStockSourceUuid() {
		return stockSourceUuid;
	}
	
	public void setStockSourceUuid(String stockSourceUuid) {
		this.stockSourceUuid = stockSourceUuid;
	}
	
	public String getStockSourceName() {
		return stockSourceName;
	}
	
	public void setStockSourceName(String stockSourceName) {
		this.stockSourceName = stockSourceName;
	}
	
	public int getStockItemId() {
		return stockItemId;
	}
	
	public void setStockItemId(int stockItemId) {
		this.stockItemId = stockItemId;
	}
	
	public String getStockItemUuid() {
		return stockItemUuid;
	}
	
	public void setStockItemUuid(String stockItemUuid) {
		this.stockItemUuid = stockItemUuid;
	}
	
}
