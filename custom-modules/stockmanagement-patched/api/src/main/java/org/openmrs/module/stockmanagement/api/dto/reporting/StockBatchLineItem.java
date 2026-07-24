package org.openmrs.module.stockmanagement.api.dto.reporting;

import java.math.BigDecimal;
import java.util.Date;

public class StockBatchLineItem {
	
	private Integer stockItemId;
	
	private Integer stockItemDrugId;
	
	private Integer stockItemConceptId;
	
	private String stockItemDrugName;
	
	private String stockItemConceptName;
	
	private String commonName;
	
	private String acronym;
	
	private Integer stockItemCategoryConceptId;
	
	private String stockItemCategoryName;
	
	private Integer stockBatchId;
	
	private String batchNo;
	
	private Date expiration;
	
	private Integer expiryNotice;
	
	private Date dateCreated;
	
	private BigDecimal reorderLevel;
	
	private String reorderLevelUoM;
	
	private BigDecimal reorderLevelFactor;
	
	private Integer reorderLevelUoMId;
	
	public Integer getStockItemId() {
		return stockItemId;
	}
	
	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}
	
	public Integer getStockItemDrugId() {
		return stockItemDrugId;
	}
	
	public void setStockItemDrugId(Integer stockItemDrugId) {
		this.stockItemDrugId = stockItemDrugId;
	}
	
	public Integer getStockItemConceptId() {
		return stockItemConceptId;
	}
	
	public void setStockItemConceptId(Integer stockItemConceptId) {
		this.stockItemConceptId = stockItemConceptId;
	}
	
	public String getStockItemDrugName() {
		return stockItemDrugName;
	}
	
	public void setStockItemDrugName(String stockItemDrugName) {
		this.stockItemDrugName = stockItemDrugName;
	}
	
	public String getStockItemConceptName() {
		return stockItemConceptName;
	}
	
	public void setStockItemConceptName(String stockItemConceptName) {
		this.stockItemConceptName = stockItemConceptName;
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
	
	public Integer getStockItemCategoryConceptId() {
		return stockItemCategoryConceptId;
	}
	
	public void setStockItemCategoryConceptId(Integer stockItemCategoryConceptId) {
		this.stockItemCategoryConceptId = stockItemCategoryConceptId;
	}
	
	public String getStockItemCategoryName() {
		return stockItemCategoryName;
	}
	
	public void setStockItemCategoryName(String stockItemCategoryName) {
		this.stockItemCategoryName = stockItemCategoryName;
	}
	
	public Integer getStockBatchId() {
		return stockBatchId;
	}
	
	public void setStockBatchId(Integer stockBatchId) {
		this.stockBatchId = stockBatchId;
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
	
	public Integer getExpiryNotice() {
		return expiryNotice;
	}
	
	public void setExpiryNotice(Integer expiryNotice) {
		this.expiryNotice = expiryNotice;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
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
	
	public Integer getReorderLevelUoMId() {
		return reorderLevelUoMId;
	}
	
	public void setReorderLevelUoMId(Integer reorderLevelUoMId) {
		this.reorderLevelUoMId = reorderLevelUoMId;
	}
}
