package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.module.stockmanagement.api.model.StockBatch;
import org.openmrs.module.stockmanagement.api.model.StockItemPackagingUOM;
import org.openmrs.module.stockmanagement.api.model.StockItemReference;
import org.openmrs.module.stockmanagement.api.model.StockSource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class StockItemDTO {
	
	private Integer id;
	
	private String uuid;
	
	private Integer drugId;
	
	private String drugUuid;
	
	private String drugName;
	
	private Integer conceptId;
	
	private String conceptUuid;
	
	private String conceptName;
	
	private Boolean hasExpiration;
	
	private List<StockBatchDTO> stockBatches;
	
	private Integer preferredVendorId;
	
	private String preferredVendorUuid;
	
	private String preferredVendorName;
	
	private BigDecimal purchasePrice;
	
	private Integer purchasePriceUoMId;
	
	private String purchasePriceUoMUuid;
	
	private Integer purchasePriceConceptId;
	
	private String purchasePriceUoMName;
	
	private BigDecimal purchasePriceUoMFactor;
	
	private Integer dispensingUnitId;
	
	private String dispensingUnitName;
	
	private String dispensingUnitUuid;
	
	private Integer dispensingUnitPackagingUoMId;
	
	private String dispensingUnitPackagingUoMUuid;
	
	private Integer dispensingUnitPackagingConceptId;
	
	private String dispensingUnitPackagingUoMName;
	
	private BigDecimal dispensingUnitPackagingUoMFactor;
	
	private Integer defaultStockOperationsUoMId;
	
	private String defaultStockOperationsUoMUuid;
	
	private Integer defaultStockOperationsConceptId;
	
	private String defaultStockOperationsUoMName;
	
	private BigDecimal defaultStockOperationsUoMFactor;
	
	private List<StockItemPackagingUOMDTO> stockItemPackagingUOMs;
	
	private List<StockItemReference> stockItemReferences;
	
	private boolean voided;
	
	private Integer creator;
	
	private Date dateCreated;
	
	private String creatorGivenName;
	
	private String creatorFamilyName;
	
	private String commonName;
	
	private String acronym;
	
	private BigDecimal reorderLevel;
	
	private Integer reorderLevelUoMId;
	
	private String reorderLevelUoMUuid;
	
	private Integer reorderLevelConceptId;
	
	private String reorderLevelUoMName;
	
	private BigDecimal reorderLevelUoMFactor;
	
	private String drugStrength;
	
	private Integer categoryId;
	
	private String categoryUuid;
	
	private String categoryName;
	
	private Integer expiryNotice;
	
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
	
	public String getDrugName() {
		return drugName;
	}
	
	public void setDrugName(String drugName) {
		this.drugName = drugName;
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
	
	public String getConceptName() {
		return conceptName;
	}
	
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	
	public Boolean getHasExpiration() {
		return hasExpiration;
	}
	
	public void setHasExpiration(Boolean hasExpiration) {
		this.hasExpiration = hasExpiration;
	}
	
	public List<StockBatchDTO> getStockBatches() {
		return stockBatches;
	}
	
	public void setStockBatches(List<StockBatchDTO> stockBatches) {
		this.stockBatches = stockBatches;
	}
	
	public Integer getPreferredVendorId() {
		return preferredVendorId;
	}
	
	public void setPreferredVendorId(Integer preferredVendorId) {
		this.preferredVendorId = preferredVendorId;
	}
	
	public String getPreferredVendorUuid() {
		return preferredVendorUuid;
	}
	
	public void setPreferredVendorUuid(String preferredVendorUuid) {
		this.preferredVendorUuid = preferredVendorUuid;
	}
	
	public String getPreferredVendorName() {
		return preferredVendorName;
	}
	
	public void setPreferredVendorName(String preferredVendorName) {
		this.preferredVendorName = preferredVendorName;
	}
	
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	
	public Integer getPurchasePriceUoMId() {
		return purchasePriceUoMId;
	}
	
	public void setPurchasePriceUoMId(Integer purchasePriceUoMId) {
		this.purchasePriceUoMId = purchasePriceUoMId;
	}
	
	public String getPurchasePriceUoMUuid() {
		return purchasePriceUoMUuid;
	}
	
	public void setPurchasePriceUoMUuid(String purchasePriceUoMUuid) {
		this.purchasePriceUoMUuid = purchasePriceUoMUuid;
	}
	
	public Integer getPurchasePriceConceptId() {
		return purchasePriceConceptId;
	}
	
	public void setPurchasePriceConceptId(Integer purchasePriceConceptId) {
		this.purchasePriceConceptId = purchasePriceConceptId;
	}
	
	public String getPurchasePriceUoMName() {
		return purchasePriceUoMName;
	}
	
	public void setPurchasePriceUoMName(String purchasePriceUoMName) {
		this.purchasePriceUoMName = purchasePriceUoMName;
	}
	
	public Integer getDispensingUnitId() {
		return dispensingUnitId;
	}
	
	public void setDispensingUnitId(Integer dispensingUnitId) {
		this.dispensingUnitId = dispensingUnitId;
	}
	
	public String getDispensingUnitName() {
		return dispensingUnitName;
	}
	
	public void setDispensingUnitName(String dispensingUnitName) {
		this.dispensingUnitName = dispensingUnitName;
	}
	
	public String getDispensingUnitUuid() {
		return dispensingUnitUuid;
	}
	
	public void setDispensingUnitUuid(String dispensingUnitUuid) {
		this.dispensingUnitUuid = dispensingUnitUuid;
	}
	
	public Integer getDispensingUnitPackagingUoMId() {
		return dispensingUnitPackagingUoMId;
	}
	
	public void setDispensingUnitPackagingUoMId(Integer dispensingUnitPackagingUoMId) {
		this.dispensingUnitPackagingUoMId = dispensingUnitPackagingUoMId;
	}
	
	public String getDispensingUnitPackagingUoMUuid() {
		return dispensingUnitPackagingUoMUuid;
	}
	
	public void setDispensingUnitPackagingUoMUuid(String dispensingUnitPackagingUoMUuid) {
		this.dispensingUnitPackagingUoMUuid = dispensingUnitPackagingUoMUuid;
	}
	
	public Integer getDispensingUnitPackagingConceptId() {
		return dispensingUnitPackagingConceptId;
	}
	
	public void setDispensingUnitPackagingConceptId(Integer dispensingUnitPackagingConceptId) {
		this.dispensingUnitPackagingConceptId = dispensingUnitPackagingConceptId;
	}
	
	public String getDispensingUnitPackagingUoMName() {
		return dispensingUnitPackagingUoMName;
	}
	
	public void setDispensingUnitPackagingUoMName(String dispensingUnitPackagingUoMName) {
		this.dispensingUnitPackagingUoMName = dispensingUnitPackagingUoMName;
	}
	
	public Integer getDefaultStockOperationsUoMId() {
		return defaultStockOperationsUoMId;
	}
	
	public void setDefaultStockOperationsUoMId(Integer defaultStockOperationsUoMId) {
		this.defaultStockOperationsUoMId = defaultStockOperationsUoMId;
	}
	
	public String getDefaultStockOperationsUoMUuid() {
		return defaultStockOperationsUoMUuid;
	}
	
	public void setDefaultStockOperationsUoMUuid(String defaultStockOperationsUoMUuid) {
		this.defaultStockOperationsUoMUuid = defaultStockOperationsUoMUuid;
	}
	
	public Integer getDefaultStockOperationsConceptId() {
		return defaultStockOperationsConceptId;
	}
	
	public void setDefaultStockOperationsConceptId(Integer defaultStockOperationsConceptId) {
		this.defaultStockOperationsConceptId = defaultStockOperationsConceptId;
	}
	
	public String getDefaultStockOperationsUoMName() {
		return defaultStockOperationsUoMName;
	}
	
	public void setDefaultStockOperationsUoMName(String defaultStockOperationsUoMName) {
		this.defaultStockOperationsUoMName = defaultStockOperationsUoMName;
	}
	
	public List<StockItemPackagingUOMDTO> getStockItemPackagingUOMs() {
		return stockItemPackagingUOMs;
	}
	
	public void setStockItemPackagingUOMs(List<StockItemPackagingUOMDTO> stockItemPackagingUOMs) {
		this.stockItemPackagingUOMs = stockItemPackagingUOMs;
	}
	
	public List<StockItemReference> getStockItemReferences() {
		return stockItemReferences;
	}
	
	public void setStockItemReferences(List<StockItemReference> stockItemReferences) {
		this.stockItemReferences = stockItemReferences;
	}
	
	public boolean getVoided() {
		return voided;
	}
	
	public void setVoided(boolean voided) {
		this.voided = voided;
	}
	
	public Integer getCreator() {
		return creator;
	}
	
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public String getCreatorGivenName() {
		return creatorGivenName;
	}
	
	public void setCreatorGivenName(String creatorGivenName) {
		this.creatorGivenName = creatorGivenName;
	}
	
	public String getCreatorFamilyName() {
		return creatorFamilyName;
	}
	
	public void setCreatorFamilyName(String creatorFamilyName) {
		this.creatorFamilyName = creatorFamilyName;
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
	
	public BigDecimal getReorderLevel() {
		return reorderLevel;
	}
	
	public void setReorderLevel(BigDecimal reorderLevel) {
		this.reorderLevel = reorderLevel;
	}
	
	public Integer getReorderLevelUoMId() {
		return reorderLevelUoMId;
	}
	
	public void setReorderLevelUoMId(Integer reorderLevelUoMId) {
		this.reorderLevelUoMId = reorderLevelUoMId;
	}
	
	public String getReorderLevelUoMUuid() {
		return reorderLevelUoMUuid;
	}
	
	public void setReorderLevelUoMUuid(String reorderLevelUoMUuid) {
		this.reorderLevelUoMUuid = reorderLevelUoMUuid;
	}
	
	public Integer getReorderLevelConceptId() {
		return reorderLevelConceptId;
	}
	
	public void setReorderLevelConceptId(Integer reorderLevelConceptId) {
		this.reorderLevelConceptId = reorderLevelConceptId;
	}
	
	public String getReorderLevelUoMName() {
		return reorderLevelUoMName;
	}
	
	public void setReorderLevelUoMName(String reorderLevelUoMName) {
		this.reorderLevelUoMName = reorderLevelUoMName;
	}
	
	public String getDrugStrength() {
		return drugStrength;
	}
	
	public void setDrugStrength(String drugStrength) {
		this.drugStrength = drugStrength;
	}
	
	public Integer getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getCategoryUuid() {
		return categoryUuid;
	}
	
	public void setCategoryUuid(String categoryUuid) {
		this.categoryUuid = categoryUuid;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public Integer getExpiryNotice() {
		return expiryNotice;
	}
	
	public void setExpiryNotice(Integer expiryNotice) {
		this.expiryNotice = expiryNotice;
	}
	
	public BigDecimal getPurchasePriceUoMFactor() {
		return purchasePriceUoMFactor;
	}
	
	public void setPurchasePriceUoMFactor(BigDecimal purchasePriceUoMFactor) {
		this.purchasePriceUoMFactor = purchasePriceUoMFactor;
	}
	
	public BigDecimal getDispensingUnitPackagingUoMFactor() {
		return dispensingUnitPackagingUoMFactor;
	}
	
	public void setDispensingUnitPackagingUoMFactor(BigDecimal dispensingUnitPackagingUoMFactor) {
		this.dispensingUnitPackagingUoMFactor = dispensingUnitPackagingUoMFactor;
	}
	
	public BigDecimal getDefaultStockOperationsUoMFactor() {
		return defaultStockOperationsUoMFactor;
	}
	
	public void setDefaultStockOperationsUoMFactor(BigDecimal defaultStockOperationsUoMFactor) {
		this.defaultStockOperationsUoMFactor = defaultStockOperationsUoMFactor;
	}
	
	public BigDecimal getReorderLevelUoMFactor() {
		return reorderLevelUoMFactor;
	}
	
	public void setReorderLevelUoMFactor(BigDecimal reorderLevelUoMFactor) {
		this.reorderLevelUoMFactor = reorderLevelUoMFactor;
	}
}
