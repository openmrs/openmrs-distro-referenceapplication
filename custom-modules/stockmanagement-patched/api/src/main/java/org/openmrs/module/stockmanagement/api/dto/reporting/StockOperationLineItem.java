package org.openmrs.module.stockmanagement.api.dto.reporting;

import org.openmrs.module.stockmanagement.api.dto.StockItemPackagingUOMDTO;
import org.openmrs.module.stockmanagement.api.model.StockOperationStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class StockOperationLineItem {
	
	private Integer stockOperationItemId;
	
	private Integer stockOperationId;
	
	private Integer stockItemId;
	
	private Integer stockItemDrugId;
	
	private Integer stockItemConceptId;
	
	private String stockItemDrugName;
	
	private String stockItemConceptName;
	
	private String commonName;
	
	private String acronym;
	
	private String batchNo;
	
	private Date expiration;
	
	private BigDecimal quantity;
	
	private BigDecimal purchasePrice;
	
	private Integer packagingUoMId;
	
	private String stockItemPackagingUOMName;
	
	private BigDecimal stockItemPackagingUOMFactor;
	
	private Integer stockItemCategoryConceptId;
	
	private String stockItemCategoryName;
	
	private BigDecimal quantityRequested;
	
	private BigDecimal quantityRequestedPackagingUOMFactor;
	
	private Integer quantityRequestedPackagingUoMId;
	
	private String quantityRequestedPackagingUOMName;
	
	// Stock Operation
	private String operationTypeName;
	
	private Date operationDate;
	
	private String operationNumber;
	
	private Integer completedBy;
	
	private String completedByGivenName;
	
	private String completedByFamilyName;
	
	private Date completedDate;
	
	private String sourceName;
	
	private StockOperationStatus stockOperationStatus;
	
	private String destinationName;
	
	private Integer reasonId;
	
	private String reasonName;
	
	private Integer responsiblePerson;
	
	private String responsiblePersonGivenName;
	
	private String responsiblePersonFamilyName;
	
	private String responsiblePersonOther;
	
	private String remarks;
	
	private Integer creator;
	
	private String creatorGivenName;
	
	private String creatorFamilyName;
	
	private Date dateCreated;
	
	private String requisitionOperationNumber;
	
	public Integer getStockOperationItemId() {
		return stockOperationItemId;
	}
	
	public void setStockOperationItemId(Integer stockOperationItemId) {
		this.stockOperationItemId = stockOperationItemId;
	}
	
	public Integer getStockOperationId() {
		return stockOperationId;
	}
	
	public void setStockOperationId(Integer stockOperationId) {
		this.stockOperationId = stockOperationId;
	}
	
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
	
	public Integer getPackagingUoMId() {
		return packagingUoMId;
	}
	
	public void setPackagingUoMId(Integer packagingUoMId) {
		this.packagingUoMId = packagingUoMId;
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
	
	public Integer getStockItemCategoryConceptId() {
		return stockItemCategoryConceptId;
	}
	
	public void setStockItemCategoryConceptId(Integer stockItemCategoryConceptId) {
		this.stockItemCategoryConceptId = stockItemCategoryConceptId;
	}
	
	public BigDecimal getQuantityRequested() {
		return quantityRequested;
	}
	
	public void setQuantityRequested(BigDecimal quantityRequested) {
		this.quantityRequested = quantityRequested;
	}
	
	public BigDecimal getQuantityRequestedPackagingUOMFactor() {
		return quantityRequestedPackagingUOMFactor;
	}
	
	public void setQuantityRequestedPackagingUOMFactor(BigDecimal quantityRequestedPackagingUOMFactor) {
		this.quantityRequestedPackagingUOMFactor = quantityRequestedPackagingUOMFactor;
	}
	
	public Integer getQuantityRequestedPackagingUoMId() {
		return quantityRequestedPackagingUoMId;
	}
	
	public void setQuantityRequestedPackagingUoMId(Integer quantityRequestedPackagingUoMId) {
		this.quantityRequestedPackagingUoMId = quantityRequestedPackagingUoMId;
	}
	
	public String getQuantityRequestedPackagingUOMName() {
		return quantityRequestedPackagingUOMName;
	}
	
	public void setQuantityRequestedPackagingUOMName(String quantityRequestedPackagingUOMName) {
		this.quantityRequestedPackagingUOMName = quantityRequestedPackagingUOMName;
	}
	
	public String getOperationTypeName() {
		return operationTypeName;
	}
	
	public void setOperationTypeName(String operationTypeName) {
		this.operationTypeName = operationTypeName;
	}
	
	public Date getOperationDate() {
		return operationDate;
	}
	
	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}
	
	public String getOperationNumber() {
		return operationNumber;
	}
	
	public void setOperationNumber(String operationNumber) {
		this.operationNumber = operationNumber;
	}
	
	public Integer getCompletedBy() {
		return completedBy;
	}
	
	public void setCompletedBy(Integer completedBy) {
		this.completedBy = completedBy;
	}
	
	public String getCompletedByGivenName() {
		return completedByGivenName;
	}
	
	public void setCompletedByGivenName(String completedByGivenName) {
		this.completedByGivenName = completedByGivenName;
	}
	
	public String getCompletedByFamilyName() {
		return completedByFamilyName;
	}
	
	public void setCompletedByFamilyName(String completedByFamilyName) {
		this.completedByFamilyName = completedByFamilyName;
	}
	
	public Date getCompletedDate() {
		return completedDate;
	}
	
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	
	public String getSourceName() {
		return sourceName;
	}
	
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	public String getDestinationName() {
		return destinationName;
	}
	
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	
	public Integer getReasonId() {
		return reasonId;
	}
	
	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}
	
	public String getReasonName() {
		return reasonName;
	}
	
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}
	
	public Integer getResponsiblePerson() {
		return responsiblePerson;
	}
	
	public void setResponsiblePerson(Integer responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}
	
	public String getResponsiblePersonGivenName() {
		return responsiblePersonGivenName;
	}
	
	public void setResponsiblePersonGivenName(String responsiblePersonGivenName) {
		this.responsiblePersonGivenName = responsiblePersonGivenName;
	}
	
	public String getResponsiblePersonFamilyName() {
		return responsiblePersonFamilyName;
	}
	
	public void setResponsiblePersonFamilyName(String responsiblePersonFamilyName) {
		this.responsiblePersonFamilyName = responsiblePersonFamilyName;
	}
	
	public String getResponsiblePersonOther() {
		return responsiblePersonOther;
	}
	
	public void setResponsiblePersonOther(String responsiblePersonOther) {
		this.responsiblePersonOther = responsiblePersonOther;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public Integer getCreator() {
		return creator;
	}
	
	public void setCreator(Integer creator) {
		this.creator = creator;
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
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public String getStockItemCategoryName() {
		return stockItemCategoryName;
	}
	
	public void setStockItemCategoryName(String stockItemCategoryName) {
		this.stockItemCategoryName = stockItemCategoryName;
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
	
	public String getRequisitionOperationNumber() {
		return requisitionOperationNumber;
	}
	
	public void setRequisitionOperationNumber(String requisitionOperationNumber) {
		this.requisitionOperationNumber = requisitionOperationNumber;
	}
	
	public StockOperationStatus getStockOperationStatus() {
		return stockOperationStatus;
	}
	
	public void setStockOperationStatus(StockOperationStatus stockOperationStatus) {
		this.stockOperationStatus = stockOperationStatus;
	}
}
