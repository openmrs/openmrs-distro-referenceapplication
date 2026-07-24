package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.Order;

import java.math.BigDecimal;
import java.util.Date;

public class OrderItemDTO {
	
	private Integer id;
	
	private String uuid;
	
	private Integer orderId;
	
	private String orderUuid;
	
	private Order.Action action;
	
	private String orderNumber;
	
	private Date scheduledDate;
	
	private String encounterUuid;
	
	private Integer stockItemId;
	
	private String stockItemUuid;
	
	private Integer drugId;
	
	private String drugUuid;
	
	private String drugName;
	
	private String conceptUuid;
	
	private Integer conceptId;
	
	private String conceptName;
	
	private String commonName;
	
	private String acronym;
	
	private BigDecimal quantity;
	
	private Integer duration;
	
	private Integer stockItemPackagingUOMId;
	
	private Integer stockItemPackagingUOMConceptId;
	
	private String stockItemPackagingUOMUuid;
	
	private String stockItemPackagingUOMName;
	
	private String createdFromName;
	
	private Integer createdFrom;
	
	private String createdFromUuid;
	
	private String createdFromPartyUuid;
	
	private String fulfilmentLocationName;
	
	private Integer fulfilmentLocationId;
	
	private String fulfilmentLocationUuid;
	
	private String fulfilmentPartyUuid;
	
	private boolean voided;
	
	private Integer creator;
	
	private Date dateCreated;
	
	private String creatorGivenName;
	
	private String creatorFamilyName;
	
	private Integer patientId;
	
	private String patientGivenName;
	
	private String patientFamilyName;
	
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
	
	public Integer getOrderId() {
		return orderId;
	}
	
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	public String getOrderUuid() {
		return orderUuid;
	}
	
	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}
	
	public Order.Action getAction() {
		return action;
	}
	
	public void setAction(Order.Action action) {
		this.action = action;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}
	
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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
	
	public String getConceptUuid() {
		return conceptUuid;
	}
	
	public void setConceptUuid(String conceptUuid) {
		this.conceptUuid = conceptUuid;
	}
	
	public Integer getConceptId() {
		return conceptId;
	}
	
	public void setConceptId(Integer conceptId) {
		this.conceptId = conceptId;
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
	
	public Integer getStockItemPackagingUOMId() {
		return stockItemPackagingUOMId;
	}
	
	public void setStockItemPackagingUOMId(Integer stockItemPackagingUOMId) {
		this.stockItemPackagingUOMId = stockItemPackagingUOMId;
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
	
	public String getCreatedFromName() {
		return createdFromName;
	}
	
	public void setCreatedFromName(String createdFromName) {
		this.createdFromName = createdFromName;
	}
	
	public Integer getCreatedFrom() {
		return createdFrom;
	}
	
	public void setCreatedFrom(Integer createdFrom) {
		this.createdFrom = createdFrom;
	}
	
	public String getCreatedFromUuid() {
		return createdFromUuid;
	}
	
	public void setCreatedFromUuid(String createdFromUuid) {
		this.createdFromUuid = createdFromUuid;
	}
	
	public String getCreatedFromPartyUuid() {
		return createdFromPartyUuid;
	}
	
	public void setCreatedFromPartyUuid(String createdFromPartyUuid) {
		this.createdFromPartyUuid = createdFromPartyUuid;
	}
	
	public String getFulfilmentLocationName() {
		return fulfilmentLocationName;
	}
	
	public void setFulfilmentLocationName(String fulfilmentLocationName) {
		this.fulfilmentLocationName = fulfilmentLocationName;
	}
	
	public Integer getFulfilmentLocationId() {
		return fulfilmentLocationId;
	}
	
	public void setFulfilmentLocationId(Integer fulfilmentLocationId) {
		this.fulfilmentLocationId = fulfilmentLocationId;
	}
	
	public String getFulfilmentLocationUuid() {
		return fulfilmentLocationUuid;
	}
	
	public void setFulfilmentLocationUuid(String fulfilmentLocationUuid) {
		this.fulfilmentLocationUuid = fulfilmentLocationUuid;
	}
	
	public String getFulfilmentPartyUuid() {
		return fulfilmentPartyUuid;
	}
	
	public void setFulfilmentPartyUuid(String fulfilmentPartyUuid) {
		this.fulfilmentPartyUuid = fulfilmentPartyUuid;
	}
	
	public String getEncounterUuid() {
		return encounterUuid;
	}
	
	public void setEncounterUuid(String encounterUuid) {
		this.encounterUuid = encounterUuid;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public Integer getDuration() {
		return duration;
	}
	
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	public Integer getStockItemPackagingUOMConceptId() {
		return stockItemPackagingUOMConceptId;
	}
	
	public void setStockItemPackagingUOMConceptId(Integer stockItemPackagingUOMConceptId) {
		this.stockItemPackagingUOMConceptId = stockItemPackagingUOMConceptId;
	}
	
	public boolean isVoided() {
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
	
	public String getPatientFamilyName() {
		return patientFamilyName;
	}
	
	public void setPatientFamilyName(String patientFamilyName) {
		this.patientFamilyName = patientFamilyName;
	}
	
	public Date getScheduledDate() {
		return scheduledDate;
	}
	
	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
}
