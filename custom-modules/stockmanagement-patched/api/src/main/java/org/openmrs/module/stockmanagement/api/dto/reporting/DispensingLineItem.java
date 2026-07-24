package org.openmrs.module.stockmanagement.api.dto.reporting;

import org.openmrs.module.stockmanagement.api.model.StockOperationStatus;

import java.math.BigDecimal;
import java.util.Date;

public class DispensingLineItem {
	
	private Integer stockItemTransactionId;
	
	private Date dateCreated;
	
	private Integer creator;
	
	private String creatorGivenName;
	
	private String creatorFamilyName;
	
	private Integer partyId;
	
	private String partyName;
	
	private Integer patientId;
	
	private String patientGivenName;
	
	private String patientMiddleName;
	
	private String patientFamilyName;
	
	private Integer orderId;
	
	private String orderNumber;
	
	private Date orderDateCreated;
	
	private Integer encounterId;
	
	private BigDecimal quantity;
	
	private Integer packagingUoMId;
	
	private String stockItemPackagingUOMName;
	
	private BigDecimal stockItemPackagingUOMFactor;
	
	private String batchNo;
	
	private Date expiration;
	
	private Integer stockItemId;
	
	private Integer stockItemDrugId;
	
	private Integer stockItemConceptId;
	
	private String stockItemDrugName;
	
	private String stockItemConceptName;
	
	private String commonName;
	
	private String acronym;
	
	private Integer stockItemCategoryConceptId;
	
	private String stockItemCategoryName;
	
	private Double dose;
	
	private Integer doseUnitsConceptId;
	
	private String doseUnitsConceptName;
	
	private Double frequencyPerDay;
	
	private Integer frequencyConceptId;
	
	private String frequencyConceptName;
	
	private Boolean asNeeded;
	
	private Integer quantityUnitsConceptId;
	
	private String quantityUnitsConceptName;
	
	private String asNeededCondition;
	
	private Integer numRefills;
	
	private Integer duration;
	
	private Integer durationUnitsConceptId;
	
	private String durationUnitsConceptName;
	
	private Integer routeConceptId;
	
	private Boolean dispenseAsWritten;
	
	private String patientIdentifier;
	
	public Integer getStockItemTransactionId() {
		return stockItemTransactionId;
	}
	
	public void setStockItemTransactionId(Integer stockItemTransactionId) {
		this.stockItemTransactionId = stockItemTransactionId;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
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
	
	public Integer getPartyId() {
		return partyId;
	}
	
	public void setPartyId(Integer partyId) {
		this.partyId = partyId;
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
	
	public String getPatientGivenName() {
		return patientGivenName;
	}
	
	public void setPatientGivenName(String patientGivenName) {
		this.patientGivenName = patientGivenName;
	}
	
	public String getPatientMiddleName() {
		return patientMiddleName;
	}
	
	public void setPatientMiddleName(String patientMiddleName) {
		this.patientMiddleName = patientMiddleName;
	}
	
	public String getPatientFamilyName() {
		return patientFamilyName;
	}
	
	public void setPatientFamilyName(String patientFamilyName) {
		this.patientFamilyName = patientFamilyName;
	}
	
	public Integer getOrderId() {
		return orderId;
	}
	
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}
	
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public Date getOrderDateCreated() {
		return orderDateCreated;
	}
	
	public void setOrderDateCreated(Date orderDateCreated) {
		this.orderDateCreated = orderDateCreated;
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
	
	public Double getDose() {
		return dose;
	}
	
	public void setDose(Double dose) {
		this.dose = dose;
	}
	
	public Integer getDoseUnitsConceptId() {
		return doseUnitsConceptId;
	}
	
	public void setDoseUnitsConceptId(Integer doseUnitsConceptId) {
		this.doseUnitsConceptId = doseUnitsConceptId;
	}
	
	public String getDoseUnitsConceptName() {
		return doseUnitsConceptName;
	}
	
	public void setDoseUnitsConceptName(String doseUnitsConceptName) {
		this.doseUnitsConceptName = doseUnitsConceptName;
	}
	
	public Double getFrequencyPerDay() {
		return frequencyPerDay;
	}
	
	public void setFrequencyPerDay(Double frequencyPerDay) {
		this.frequencyPerDay = frequencyPerDay;
	}
	
	public Integer getFrequencyConceptId() {
		return frequencyConceptId;
	}
	
	public void setFrequencyConceptId(Integer frequencyConceptId) {
		this.frequencyConceptId = frequencyConceptId;
	}
	
	public String getFrequencyConceptName() {
		return frequencyConceptName;
	}
	
	public void setFrequencyConceptName(String frequencyConceptName) {
		this.frequencyConceptName = frequencyConceptName;
	}
	
	public Boolean getAsNeeded() {
		return asNeeded;
	}
	
	public void setAsNeeded(Boolean asNeeded) {
		this.asNeeded = asNeeded;
	}
	
	public Integer getQuantityUnitsConceptId() {
		return quantityUnitsConceptId;
	}
	
	public void setQuantityUnitsConceptId(Integer quantityUnitsConceptId) {
		this.quantityUnitsConceptId = quantityUnitsConceptId;
	}
	
	public String getQuantityUnitsConceptName() {
		return quantityUnitsConceptName;
	}
	
	public void setQuantityUnitsConceptName(String quantityUnitsConceptName) {
		this.quantityUnitsConceptName = quantityUnitsConceptName;
	}
	
	public String getAsNeededCondition() {
		return asNeededCondition;
	}
	
	public void setAsNeededCondition(String asNeededCondition) {
		this.asNeededCondition = asNeededCondition;
	}
	
	public Integer getNumRefills() {
		return numRefills;
	}
	
	public void setNumRefills(Integer numRefills) {
		this.numRefills = numRefills;
	}
	
	public Integer getDuration() {
		return duration;
	}
	
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	public Integer getDurationUnitsConceptId() {
		return durationUnitsConceptId;
	}
	
	public void setDurationUnitsConceptId(Integer durationUnitsConceptId) {
		this.durationUnitsConceptId = durationUnitsConceptId;
	}
	
	public String getDurationUnitsConceptName() {
		return durationUnitsConceptName;
	}
	
	public void setDurationUnitsConceptName(String durationUnitsConceptName) {
		this.durationUnitsConceptName = durationUnitsConceptName;
	}
	
	public Integer getRouteConceptId() {
		return routeConceptId;
	}
	
	public void setRouteConceptId(Integer routeConceptId) {
		this.routeConceptId = routeConceptId;
	}
	
	public Boolean getDispenseAsWritten() {
		return dispenseAsWritten;
	}
	
	public void setDispenseAsWritten(Boolean dispenseAsWritten) {
		this.dispenseAsWritten = dispenseAsWritten;
	}
	
	public Integer getStockItemId() {
		return stockItemId;
	}
	
	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}
	
	public String getPatientIdentifier() {
		return patientIdentifier;
	}
	
	public void setPatientIdentifier(String patientIdentifier) {
		this.patientIdentifier = patientIdentifier;
	}
}
