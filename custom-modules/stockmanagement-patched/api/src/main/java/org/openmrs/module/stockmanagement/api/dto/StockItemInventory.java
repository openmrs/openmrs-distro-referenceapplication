package org.openmrs.module.stockmanagement.api.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class StockItemInventory {
	
	private String uuid;
	
	private Integer partyId;
	
	private String partyUuid;
	
	private String locationUuid;
	
	private String partyName;
	
	private Integer stockItemId;
	
	private String stockItemUuid;
	
	private String drugName;
	
	private String conceptName;
	
	private String commonName;
	
	private String acronym;
	
	private Integer drugId;
	
	private String drugUuid;
	
	private String drugStrength;
	
	private Integer conceptId;
	
	private String conceptUuid;
	
	private Integer stockBatchId;
	
	private String stockBatchUuid;
	
	private String batchNumber;
	
	private BigDecimal quantity;
	
	private String quantityUoM;
	
	public String quantityUoMUuid;
	
	private BigDecimal quantityFactor;
	
	private Date expiration;
	
	private Integer partyStockItemBatchHashCode;
	
	private String stockItemCategoryName;
	
	private BigDecimal reorderLevel;
	
	private String reorderLevelUoM;
	
	private BigDecimal reorderLevelFactor;
	
	public String getDrugStrength() {
		return drugStrength;
	}
	
	public void setDrugStrength(String drugStrength) {
		this.drugStrength = drugStrength;
	}
	
	public Integer getDrugId() {
		return drugId;
	}
	
	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}
	
	public String getDrugUuid() {
		return drugUuid;
	}
	
	public void setDrugUuid(String drugUuid) {
		this.drugUuid = drugUuid;
	}
	
	public Integer getConceptId() {
		return conceptId;
	}
	
	public void setConceptId(Integer conceptId) {
		this.conceptId = conceptId;
	}
	
	public String getConceptUuid() {
		return conceptUuid;
	}
	
	public void setConceptUuid(String conceptUuid) {
		this.conceptUuid = conceptUuid;
	}
	
	public String getLocationUuid() {
		return locationUuid;
	}
	
	public void setLocationUuid(String locationUuid) {
		this.locationUuid = locationUuid;
	}
	
	public StockItemInventory() {
		uuid = UUID.randomUUID().toString();
	}
	
	public Integer getPartyId() {
		return partyId;
	}
	
	public void setPartyId(Integer partyId) {
		this.partyId = partyId;
	}
	
	public String getPartyUuid() {
		return partyUuid;
	}
	
	public void setPartyUuid(String partyUuid) {
		this.partyUuid = partyUuid;
	}
	
	public Integer getStockItemId() {
		return stockItemId;
	}
	
	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}
	
	public String getStockItemUuid() {
		return stockItemUuid;
	}
	
	public void setStockItemUuid(String stockItemUuid) {
		this.stockItemUuid = stockItemUuid;
	}
	
	public Integer getStockBatchId() {
		return stockBatchId;
	}
	
	public void setStockBatchId(Integer stockBatchId) {
		this.stockBatchId = stockBatchId;
	}
	
	public String getStockBatchUuid() {
		return stockBatchUuid;
	}
	
	public void setStockBatchUuid(String stockBatchUuid) {
		this.stockBatchUuid = stockBatchUuid;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public String getPartyName() {
		return partyName;
	}
	
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	
	public String getBatchNumber() {
		return batchNumber;
	}
	
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	
	public String getQuantityUoM() {
		return quantityUoM;
	}
	
	public void setQuantityUoM(String quantityUoM) {
		this.quantityUoM = quantityUoM;
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
	
	public String getDrugName() {
		return drugName;
	}
	
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	
	public String getConceptName() {
		return conceptName;
	}
	
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	
	public String getCommonName() {
		return commonName;
	}
	
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	
	public String getAcronym() {
		return acronym;
	}
	
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	
	public String getQuantityUoMUuid() {
		return quantityUoMUuid;
	}
	
	public void setQuantityUoMUuid(String quantityUoMUuid) {
		this.quantityUoMUuid = quantityUoMUuid;
	}
	
	public BigDecimal getQuantityFactor() {
		return quantityFactor;
	}
	
	public BigDecimal getReorderLevel() {
		return reorderLevel;
	}
	
	public void setReorderLevel(BigDecimal reorderLevel) {
		this.reorderLevel = reorderLevel;
	}
	
	public String getReorderLevelUoM() {
		return reorderLevelUoM;
	}
	
	public void setReorderLevelUoM(String reorderLevelUoM) {
		this.reorderLevelUoM = reorderLevelUoM;
	}
	
	public BigDecimal getReorderLevelFactor() {
		return reorderLevelFactor;
	}
	
	public void setReorderLevelFactor(BigDecimal reorderLevelFactor) {
		this.reorderLevelFactor = reorderLevelFactor;
	}
	
	public void setQuantityFactor(BigDecimal quantityFactor) {
		this.quantityFactor = quantityFactor;
	}
	
	public String getStockItemCategoryName() {
		return stockItemCategoryName;
	}
	
	public void setStockItemCategoryName(String stockItemCategoryName) {
		this.stockItemCategoryName = stockItemCategoryName;
	}
	
	public int getPartyStockItemBatchHashCode() {
		if (partyStockItemBatchHashCode == null) {
			int result = partyId != null ? partyId.hashCode() : 0;
			result = 31 * result + (stockItemId != null ? stockItemId.hashCode() : 0);
			result = 31 * result + (stockBatchId != null ? stockBatchId.hashCode() : 0);
			partyStockItemBatchHashCode = result;
		}
		return partyStockItemBatchHashCode;
	}
}
