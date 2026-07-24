package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.module.stockmanagement.api.model.StockOperationStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class StockItemTransactionDTO {
	
	private String uuid;
	
	private Date dateCreated;
	
	private String partyUuid;
	
	private Integer partyId;
	
	private String partyName;
	
	private Integer patientId;

	private String patientUuid;
	
	private Integer orderId;
	
	private Integer encounterId;
	
	private BigDecimal quantity;
	
	private String stockBatchUuid;
	
	private String stockBatchNo;
	
	private Date expiration;
	
	private String stockItemUuid;
	
	private Integer stockItemId;
	
	private String stockOperationUuid;
	
	private StockOperationStatus stockOperationStatus;
	
	private String stockOperationNumber;
	
	private String stockOperationTypeName;
	
	private String stockItemPackagingUOMUuid;
	
	private Integer packagingUoMId;
	
	private String packagingUomName;
	
	private BigDecimal packagingUomFactor;
	
	private Integer operationSourcePartyId;
	
	private String operationSourcePartyName;
	
	private Integer operationDestinationPartyId;
	
	private String operationDestinationPartyName;
	
	public Integer getPartyId() {
		return partyId;
	}
	
	public void setPartyId(Integer partyId) {
		this.partyId = partyId;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public String getPartyUuid() {
		return partyUuid;
	}
	
	public void setPartyUuid(String partyUuid) {
		this.partyUuid = partyUuid;
	}
	
	public String getPartyName() {
		return partyName;
	}
	
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	
	public Integer getPatientId() {
		return patientId;
	}
	
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	public Integer getEncounterId() {
		return encounterId;
	}
	
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public String getStockBatchUuid() {
		return stockBatchUuid;
	}
	
	public void setStockBatchUuid(String stockBatchUuid) {
		this.stockBatchUuid = stockBatchUuid;
	}
	
	public String getStockBatchNo() {
		return stockBatchNo;
	}
	
	public void setStockBatchNo(String stockBatchNo) {
		this.stockBatchNo = stockBatchNo;
	}
	
	public String getStockItemUuid() {
		return stockItemUuid;
	}
	
	public void setStockItemUuid(String stockItemUuid) {
		this.stockItemUuid = stockItemUuid;
	}
	
	public Integer getStockItemId() {
		return stockItemId;
	}
	
	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}
	
	public String getStockOperationUuid() {
		return stockOperationUuid;
	}
	
	public void setStockOperationUuid(String stockOperationUuid) {
		this.stockOperationUuid = stockOperationUuid;
	}
	
	public StockOperationStatus getStockOperationStatus() {
		return stockOperationStatus;
	}
	
	public void setStockOperationStatus(StockOperationStatus stockOperationStatus) {
		this.stockOperationStatus = stockOperationStatus;
	}
	
	public String getStockOperationNumber() {
		return stockOperationNumber;
	}
	
	public void setStockOperationNumber(String stockOperationNumber) {
		this.stockOperationNumber = stockOperationNumber;
	}
	
	public String getStockOperationTypeName() {
		return stockOperationTypeName;
	}
	
	public void setStockOperationTypeName(String stockOperationTypeName) {
		this.stockOperationTypeName = stockOperationTypeName;
	}
	
	public String getStockItemPackagingUOMUuid() {
		return stockItemPackagingUOMUuid;
	}
	
	public void setStockItemPackagingUOMUuid(String stockItemPackagingUOMUuid) {
		this.stockItemPackagingUOMUuid = stockItemPackagingUOMUuid;
	}
	
	public Integer getPackagingUoMId() {
		return packagingUoMId;
	}
	
	public void setPackagingUoMId(Integer packagingUoMId) {
		this.packagingUoMId = packagingUoMId;
	}
	
	public String getPackagingUomName() {
		return packagingUomName;
	}
	
	public void setPackagingUomName(String packagingUomName) {
		this.packagingUomName = packagingUomName;
	}
	
	public BigDecimal getPackagingUomFactor() {
		return packagingUomFactor;
	}
	
	public void setPackagingUomFactor(BigDecimal packagingUomFactor) {
		this.packagingUomFactor = packagingUomFactor;
	}
	
	public Integer getOperationSourcePartyId() {
		return operationSourcePartyId;
	}
	
	public void setOperationSourcePartyId(Integer operationSourcePartyId) {
		this.operationSourcePartyId = operationSourcePartyId;
	}
	
	public String getOperationSourcePartyName() {
		return operationSourcePartyName;
	}
	
	public void setOperationSourcePartyName(String operationSourcePartyName) {
		this.operationSourcePartyName = operationSourcePartyName;
	}
	
	public Integer getOperationDestinationPartyId() {
		return operationDestinationPartyId;
	}
	
	public void setOperationDestinationPartyId(Integer operationDestinationPartyId) {
		this.operationDestinationPartyId = operationDestinationPartyId;
	}
	
	public String getOperationDestinationPartyName() {
		return operationDestinationPartyName;
	}
	
	public void setOperationDestinationPartyName(String operationDestinationPartyName) {
		this.operationDestinationPartyName = operationDestinationPartyName;
	}
	
	public Integer getOrderId() {
		return orderId;
	}
	
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public Date getExpiration() {
		return expiration;
	}
	
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getPatientUuid() {
		return patientUuid;
	}

	public void setPatientUuid(String patientUuid) {
		this.patientUuid = patientUuid;
	}
}
