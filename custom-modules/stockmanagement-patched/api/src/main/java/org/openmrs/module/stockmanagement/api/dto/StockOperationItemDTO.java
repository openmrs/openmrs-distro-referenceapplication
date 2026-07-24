package org.openmrs.module.stockmanagement.api.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class StockOperationItemDTO {
	
	private Integer id;
	
	private String uuid;
	
	private Integer stockItemId;
	
	private String stockItemUuid;
	
	private Integer stockItemDrugId;
	
	private Integer stockItemConceptId;
	
	private String stockItemName;
	
	private String commonName;
	
	private String acronym;
	
	private String requisitionStockOperationId;
	
	private String stockItemPackagingUOMUuid;
	
	private Integer packagingUoMId;
	
	private String stockItemPackagingUOMName;
	
	private BigDecimal stockItemPackagingUOMFactor;
	
	private Integer stockBatchId;
	
	private String stockBatchUuid;
	
	private Integer stockOperationId;
	
	private String stockOperationUuid;
	
	private String batchNo;
	
	private Date expiration;
	
	private BigDecimal quantity;
	
	private BigDecimal purchasePrice;
	
	private boolean hasExpiration;
	
	private BigDecimal quantityReceived;
	
	private String quantityReceivedPackagingUOMUuid;
	
	private Integer quantityReceivedPackagingUOMUoMId;
	
	private String quantityReceivedPackagingUOMName;
	
	private BigDecimal quantityReceivedPackagingUOMFactor;
	
	private BigDecimal quantityRequested;
	
	private String quantityRequestedPackagingUOMUuid;
	
	private Integer quantityRequestedPackagingUOMUoMId;
	
	private String quantityRequestedPackagingUOMName;
	
	private BigDecimal quantityRequestedPackagingUOMFactor;
	
	private List<StockItemPackagingUOMDTO> packagingUnits;
	
	private Boolean canUpdateBatchInformation;
	
	public Integer getStockItemId() {
		return stockItemId;
	}
	
	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}
	
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
	
	public String getStockOperationUuid() {
		return stockOperationUuid;
	}
	
	public void setStockOperationUuid(String stockOperationUuid) {
		this.stockOperationUuid = stockOperationUuid;
	}
	
	public Integer getStockOperationId() {
		return stockOperationId;
	}
	
	public void setStockOperationId(Integer stockOperationId) {
		this.stockOperationId = stockOperationId;
	}
	
	public String getStockItemName() {
		return stockItemName;
	}
	
	public void setStockItemName(String stockItemName) {
		this.stockItemName = stockItemName;
	}
	
	public String getStockItemPackagingUOMUuid() {
		return stockItemPackagingUOMUuid;
	}
	
	public void setStockItemPackagingUOMUuid(String stockItemPackagingUOMUuid) {
		this.stockItemPackagingUOMUuid = stockItemPackagingUOMUuid;
	}
	
	public String getStockItemPackagingUOMName() {
		return stockItemPackagingUOMName;
	}
	
	public void setStockItemPackagingUOMName(String stockItemPackagingUOMName) {
		this.stockItemPackagingUOMName = stockItemPackagingUOMName;
	}
	
	public BigDecimal getStockItemPackagingUOMFactor() {
		return stockItemPackagingUOMFactor;
	}
	
	public void setStockItemPackagingUOMFactor(BigDecimal stockItemPackagingUOMFactor) {
		this.stockItemPackagingUOMFactor = stockItemPackagingUOMFactor;
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
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	
	public boolean getHasExpiration() {
		return hasExpiration;
	}
	
	public void setHasExpiration(boolean hasExpiration) {
		this.hasExpiration = hasExpiration;
	}
	
	public List<StockItemPackagingUOMDTO> getPackagingUnits() {
		return packagingUnits;
	}
	
	public void setPackagingUnits(List<StockItemPackagingUOMDTO> packagingUnits) {
		this.packagingUnits = packagingUnits;
	}
	
	public Integer getStockItemConceptId() {
		return stockItemConceptId;
	}
	
	public void setStockItemConceptId(Integer stockItemConceptId) {
		this.stockItemConceptId = stockItemConceptId;
	}
	
	public Integer getStockItemDrugId() {
		return stockItemDrugId;
	}
	
	public void setStockItemDrugId(Integer stockItemDrugId) {
		this.stockItemDrugId = stockItemDrugId;
	}
	
	public Integer getPackagingUoMId() {
		return packagingUoMId;
	}
	
	public String getRequisitionStockOperationId() {
		return requisitionStockOperationId;
	}
	
	public void setRequisitionStockOperationId(String requisitionStockOperationId) {
		this.requisitionStockOperationId = requisitionStockOperationId;
	}
	
	public void setPackagingUoMId(Integer packagingUoMId) {
		this.packagingUoMId = packagingUoMId;
	}
	
	public BigDecimal getQuantityReceived() {
		return quantityReceived;
	}
	
	public void setQuantityReceived(BigDecimal quantityReceived) {
		this.quantityReceived = quantityReceived;
	}
	
	public String getQuantityReceivedPackagingUOMUuid() {
		return quantityReceivedPackagingUOMUuid;
	}
	
	public void setQuantityReceivedPackagingUOMUuid(String quantityReceivedPackagingUOMUuid) {
		this.quantityReceivedPackagingUOMUuid = quantityReceivedPackagingUOMUuid;
	}
	
	public Integer getQuantityReceivedPackagingUOMUoMId() {
		return quantityReceivedPackagingUOMUoMId;
	}
	
	public void setQuantityReceivedPackagingUOMUoMId(Integer quantityReceivedPackagingUOMUoMId) {
		this.quantityReceivedPackagingUOMUoMId = quantityReceivedPackagingUOMUoMId;
	}
	
	public String getQuantityReceivedPackagingUOMName() {
		return quantityReceivedPackagingUOMName;
	}
	
	public void setQuantityReceivedPackagingUOMName(String quantityReceivedPackagingUOMName) {
		this.quantityReceivedPackagingUOMName = quantityReceivedPackagingUOMName;
	}
	
	public BigDecimal getQuantityReceivedPackagingUOMFactor() {
		return quantityReceivedPackagingUOMFactor;
	}
	
	public void setQuantityReceivedPackagingUOMFactor(BigDecimal quantityReceivedPackagingUOMFactor) {
		this.quantityReceivedPackagingUOMFactor = quantityReceivedPackagingUOMFactor;
	}
	
	public BigDecimal getQuantityRequestedPackagingUOMFactor() {
		return quantityRequestedPackagingUOMFactor;
	}
	
	public void setQuantityRequestedPackagingUOMFactor(BigDecimal quantityRequestedPackagingUOMFactor) {
		this.quantityRequestedPackagingUOMFactor = quantityRequestedPackagingUOMFactor;
	}
	
	public String getQuantityRequestedPackagingUOMName() {
		return quantityRequestedPackagingUOMName;
	}
	
	public void setQuantityRequestedPackagingUOMName(String quantityRequestedPackagingUOMName) {
		this.quantityRequestedPackagingUOMName = quantityRequestedPackagingUOMName;
	}
	
	public Integer getQuantityRequestedPackagingUOMUoMId() {
		return quantityRequestedPackagingUOMUoMId;
	}
	
	public void setQuantityRequestedPackagingUOMUoMId(Integer quantityRequestedPackagingUOMUoMId) {
		this.quantityRequestedPackagingUOMUoMId = quantityRequestedPackagingUOMUoMId;
	}
	
	public String getQuantityRequestedPackagingUOMUuid() {
		return quantityRequestedPackagingUOMUuid;
	}
	
	public void setQuantityRequestedPackagingUOMUuid(String quantityRequestedPackagingUOMUuid) {
		this.quantityRequestedPackagingUOMUuid = quantityRequestedPackagingUOMUuid;
	}
	
	public BigDecimal getQuantityRequested() {
		return quantityRequested;
	}
	
	public void setQuantityRequested(BigDecimal quantityRequested) {
		this.quantityRequested = quantityRequested;
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
	
	public Boolean getCanUpdateBatchInformation() {
		return canUpdateBatchInformation;
	}
	
	public void setCanUpdateBatchInformation(Boolean canUpdateBatchInformation) {
		this.canUpdateBatchInformation = canUpdateBatchInformation;
	}
}
