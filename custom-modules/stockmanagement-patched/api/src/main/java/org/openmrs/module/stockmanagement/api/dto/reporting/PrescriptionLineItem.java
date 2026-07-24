package org.openmrs.module.stockmanagement.api.dto.reporting;

import org.openmrs.Order;

import java.math.BigDecimal;
import java.util.Date;

public class PrescriptionLineItem {
	
	private Integer id;
	
	private Integer orderId;
	
	private Integer previousOrderId;
	
	private Date dateActivated;
	
	private Date dateStopped;
	
	private String action;
	
	private String urgency;
	
	private Integer patientId;
	
	private String patientGivenName;
	
	private String patientMiddleName;
	
	private String patientFamilyName;
	
	private Integer ordererPersonId;
	
	private String ordererGivenName;
	
	private String ordererMiddleName;
	
	private String ordererFamilyName;
	
	private Integer stockItemId;
	
	private Integer stockItemDrugId;
	
	private Integer stockItemConceptId;
	
	private String stockItemDrugName;
	
	private String stockItemConceptName;
	
	private String commonName;
	
	private String acronym;
	
	private Integer stockItemCategoryConceptId;
	
	private String stockItemCategoryName;
	
	private BigDecimal quantity;
	
	private Integer packagingUoMId;
	
	private String stockItemPackagingUOMName;
	
	private BigDecimal stockItemPackagingUOMFactor;
	
	private String createdFrom;
	
	private String fulfilmentLocation;
	
	private String fulfilmentLocationUuid;
	
	private BigDecimal dose;
	
	private Integer doseUnitsConceptId;
	
	private String doseUnitsConceptName;
	
	private BigDecimal frequencyPerDay;
	
	private Integer frequencyConceptId;
	
	private String frequencyConceptName;
	
	private Boolean asNeeded;
	
	private String dosingInstructions;
	
	private Integer quantityUnitsConceptId;
	
	private String quantityUnitsConceptName;
	
	private String asNeededCondition;
	
	private Integer numRefills;
	
	private Integer duration;
	
	private Integer durationUnitsConceptId;
	
	private String durationUnitsConceptName;
	
	private Integer routeConceptId;
	
	private String routeConceptName;
	
	private Boolean dispenseAsWritten;
	
	private Date dateCreated;
	
	private String orderNumber;
	
	private String patientIdentifier;
	
	private String batchNo;
	
	private Date batchExpiryDate;
	
	private Date dateDispensed;
	
	private String dispensingLocation;
	
	private Integer dispenserUserId;
	
	private String dispenserFamilyName;
	
	private String dispenserMiddleName;
	
	private String dispenserGivenName;
	
	private BigDecimal quantityDispensed;
	
	private Integer quantityDispensedPackagingUoMId;
	
	private String quantityDispensedStockItemPackagingUOMName;
	
	private BigDecimal quantityDispensedStockItemPackagingUOMFactor;
	
	private Integer stockItemTransactionId;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getOrderId() {
		return orderId;
	}
	
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public Integer getPreviousOrderId() {
		return previousOrderId;
	}
	
	public void setPreviousOrderId(Integer previousOrderId) {
		this.previousOrderId = previousOrderId;
	}
	
	public Date getDateActivated() {
		return dateActivated;
	}
	
	public void setDateActivated(Date dateActivated) {
		this.dateActivated = dateActivated;
	}
	
	public Date getDateStopped() {
		return dateStopped;
	}
	
	public void setDateStopped(Date dateStopped) {
		this.dateStopped = dateStopped;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getUrgency() {
		return urgency;
	}
	
	public void setUrgency(String urgency) {
		this.urgency = urgency;
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
	
	public Integer getOrdererPersonId() {
		return ordererPersonId;
	}
	
	public void setOrdererPersonId(Integer ordererPersonId) {
		this.ordererPersonId = ordererPersonId;
	}
	
	public String getOrdererGivenName() {
		return ordererGivenName;
	}
	
	public void setOrdererGivenName(String ordererGivenName) {
		this.ordererGivenName = ordererGivenName;
	}
	
	public String getOrdererMiddleName() {
		return ordererMiddleName;
	}
	
	public void setOrdererMiddleName(String ordererMiddleName) {
		this.ordererMiddleName = ordererMiddleName;
	}
	
	public String getOrdererFamilyName() {
		return ordererFamilyName;
	}
	
	public void setOrdererFamilyName(String ordererFamilyName) {
		this.ordererFamilyName = ordererFamilyName;
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
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Double quantity) {
		if (quantity != null) {
			this.quantity = BigDecimal.valueOf(quantity);
		}
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
	
	public String getCreatedFrom() {
		return createdFrom;
	}
	
	public void setCreatedFrom(String createdFrom) {
		this.createdFrom = createdFrom;
	}
	
	public String getFulfilmentLocation() {
		return fulfilmentLocation;
	}
	
	public void setFulfilmentLocation(String fulfilmentLocation) {
		this.fulfilmentLocation = fulfilmentLocation;
	}
	
	public BigDecimal getDose() {
		return dose;
	}
	
	public void setDose(Double dose) {
		if (dose != null) {
			this.dose = BigDecimal.valueOf(dose);
		}
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
	
	public BigDecimal getFrequencyPerDay() {
		return frequencyPerDay;
	}
	
	public void setFrequencyPerDay(Double frequencyPerDay) {
		if (frequencyPerDay != null) {
			this.frequencyPerDay = BigDecimal.valueOf(frequencyPerDay);
		}
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
	
	public String getDosingInstructions() {
		return dosingInstructions;
	}
	
	public void setDosingInstructions(String dosingInstructions) {
		this.dosingInstructions = dosingInstructions;
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
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}
	
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public String getRouteConceptName() {
		return routeConceptName;
	}
	
	public void setRouteConceptName(String routeConceptName) {
		this.routeConceptName = routeConceptName;
	}
	
	public String getPatientIdentifier() {
		return patientIdentifier;
	}
	
	public void setPatientIdentifier(String patientIdentifier) {
		this.patientIdentifier = patientIdentifier;
	}
	
	public String getFulfilmentLocationUuid() {
		return fulfilmentLocationUuid;
	}
	
	public void setFulfilmentLocationUuid(String fulfilmentLocationUuid) {
		this.fulfilmentLocationUuid = fulfilmentLocationUuid;
	}
	
	public BigDecimal getQuantityDispensed() {
		return quantityDispensed;
	}
	
	public void setQuantityDispensed(BigDecimal quantityDispensed) {
		this.quantityDispensed = quantityDispensed;
	}
	
	public Integer getQuantityDispensedPackagingUoMId() {
		return quantityDispensedPackagingUoMId;
	}
	
	public void setQuantityDispensedPackagingUoMId(Integer quantityDispensedPackagingUoMId) {
		this.quantityDispensedPackagingUoMId = quantityDispensedPackagingUoMId;
	}
	
	public String getQuantityDispensedStockItemPackagingUOMName() {
		return quantityDispensedStockItemPackagingUOMName;
	}
	
	public void setQuantityDispensedStockItemPackagingUOMName(String quantityDispensedStockItemPackagingUOMName) {
		this.quantityDispensedStockItemPackagingUOMName = quantityDispensedStockItemPackagingUOMName;
	}
	
	public BigDecimal getQuantityDispensedStockItemPackagingUOMFactor() {
		return quantityDispensedStockItemPackagingUOMFactor;
	}
	
	public void setQuantityDispensedStockItemPackagingUOMFactor(BigDecimal quantityDispensedStockItemPackagingUOMFactor) {
		this.quantityDispensedStockItemPackagingUOMFactor = quantityDispensedStockItemPackagingUOMFactor;
	}
	
	public String getDispenserGivenName() {
		return dispenserGivenName;
	}
	
	public void setDispenserGivenName(String dispenserGivenName) {
		this.dispenserGivenName = dispenserGivenName;
	}
	
	public String getDispenserFamilyName() {
		return dispenserFamilyName;
	}
	
	public void setDispenserFamilyName(String dispenserFamilyName) {
		this.dispenserFamilyName = dispenserFamilyName;
	}
	
	public Integer getDispenserUserId() {
		return dispenserUserId;
	}
	
	public void setDispenserUserId(Integer dispenserUserId) {
		this.dispenserUserId = dispenserUserId;
	}
	
	public String getDispensingLocation() {
		return dispensingLocation;
	}
	
	public void setDispensingLocation(String dispensingLocation) {
		this.dispensingLocation = dispensingLocation;
	}
	
	public Integer getStockItemTransactionId() {
		return stockItemTransactionId;
	}
	
	public void setStockItemTransactionId(Integer stockItemTransactionId) {
		this.stockItemTransactionId = stockItemTransactionId;
	}
	
	public Date getDateDispensed() {
		return dateDispensed;
	}
	
	public void setDateDispensed(Date dateDispensed) {
		this.dateDispensed = dateDispensed;
	}
	
	public Date getBatchExpiryDate() {
		return batchExpiryDate;
	}
	
	public void setBatchExpiryDate(Date batchExpiryDate) {
		this.batchExpiryDate = batchExpiryDate;
	}
	
	public String getBatchNo() {
		return batchNo;
	}
	
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
	public String getDispenserMiddleName() {
		return dispenserMiddleName;
	}
	
	public void setDispenserMiddleName(String dispenserMiddleName) {
		this.dispenserMiddleName = dispenserMiddleName;
	}
}
