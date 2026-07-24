package org.openmrs.module.stockmanagement.api.dto;

import java.util.Date;
import java.util.List;

public class OrderItemSearchFilter {
	
	private Integer id;
	
	private String uuid;
	
	private List<Integer> encounterIds;
	
	private List<String> encounterUuids;
	
	private Date orderDateMin;
	
	private Date orderDateMax;
	
	private List<Integer> orderIds;
	
	private List<String> orderUuids;
	
	private String orderNumber;
	
	private List<Integer> patientIds;
	
	private Boolean isDrug;
	
	private List<Integer> stockItemIds;
	
	private List<String> stockItemUuids;
	
	private List<Integer> drugIds;
	
	private List<String> drugUuids;
	
	private List<String> conceptUuids;
	
	private List<Integer> conceptIds;
	
	private boolean searchEitherDrugOrConceptStockItems = false;
	
	private List<Integer> createdFromLocationIds;
	
	private List<String> createdFromLocationUuids;
	
	private List<String> createdFromPartyUuids;
	
	private List<Integer> fulfilmentLocationIds;
	
	private List<String> fulfilmentLocationUuids;
	
	private List<String> fulfilmentPartyUuids;
	
	private Integer startIndex;
	
	private Integer limit;
	
	private boolean includeVoided = false;
	
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
	
	public List<Integer> getOrderIds() {
		return orderIds;
	}
	
	public void setOrderIds(List<Integer> orderIds) {
		this.orderIds = orderIds;
	}
	
	public List<String> getOrderUuids() {
		return orderUuids;
	}
	
	public void setOrderUuids(List<String> orderUuids) {
		this.orderUuids = orderUuids;
	}
	
	public String getOrderNumber() {
		return orderNumber;
	}
	
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	public List<Integer> getStockItemIds() {
		return stockItemIds;
	}
	
	public void setStockItemIds(List<Integer> stockItemIds) {
		this.stockItemIds = stockItemIds;
	}
	
	public List<String> getStockItemUuids() {
		return stockItemUuids;
	}
	
	public void setStockItemUuids(List<String> stockItemUuids) {
		this.stockItemUuids = stockItemUuids;
	}
	
	public List<Integer> getDrugIds() {
		return drugIds;
	}
	
	public void setDrugIds(List<Integer> drugIds) {
		this.drugIds = drugIds;
	}
	
	public List<String> getDrugUuids() {
		return drugUuids;
	}
	
	public void setDrugUuids(List<String> drugUuids) {
		this.drugUuids = drugUuids;
	}
	
	public List<String> getConceptUuids() {
		return conceptUuids;
	}
	
	public void setConceptUuids(List<String> conceptUuids) {
		this.conceptUuids = conceptUuids;
	}
	
	public List<Integer> getConceptIds() {
		return conceptIds;
	}
	
	public void setConceptIds(List<Integer> conceptIds) {
		this.conceptIds = conceptIds;
	}
	
	public List<Integer> getCreatedFromLocationIds() {
		return createdFromLocationIds;
	}
	
	public void setCreatedFromLocationIds(List<Integer> createdFromLocationIds) {
		this.createdFromLocationIds = createdFromLocationIds;
	}
	
	public List<String> getCreatedFromLocationUuids() {
		return createdFromLocationUuids;
	}
	
	public void setCreatedFromLocationUuids(List<String> createdFromLocationUuids) {
		this.createdFromLocationUuids = createdFromLocationUuids;
	}
	
	public List<String> getCreatedFromPartyUuids() {
		return createdFromPartyUuids;
	}
	
	public void setCreatedFromPartyUuids(List<String> createdFromPartyUuids) {
		this.createdFromPartyUuids = createdFromPartyUuids;
	}
	
	public List<Integer> getFulfilmentLocationIds() {
		return fulfilmentLocationIds;
	}
	
	public void setFulfilmentLocationIds(List<Integer> fulfilmentLocationIds) {
		this.fulfilmentLocationIds = fulfilmentLocationIds;
	}
	
	public List<String> getFulfilmentLocationUuids() {
		return fulfilmentLocationUuids;
	}
	
	public void setFulfilmentLocationUuids(List<String> fulfilmentLocationUuids) {
		this.fulfilmentLocationUuids = fulfilmentLocationUuids;
	}
	
	public List<String> getFulfilmentPartyUuids() {
		return fulfilmentPartyUuids;
	}
	
	public void setFulfilmentPartyUuids(List<String> fulfilmentPartyUuids) {
		this.fulfilmentPartyUuids = fulfilmentPartyUuids;
	}
	
	public boolean getSearchEitherDrugOrConceptStockItems() {
		return searchEitherDrugOrConceptStockItems;
	}
	
	public void setSearchEitherDrugOrConceptStockItems(boolean searchEitherDrugOrConceptStockItems) {
		this.searchEitherDrugOrConceptStockItems = searchEitherDrugOrConceptStockItems;
	}
	
	public List<Integer> getPatientIds() {
		return patientIds;
	}
	
	public void setPatientIds(List<Integer> patientIds) {
		this.patientIds = patientIds;
	}
	
	public boolean getIncludeVoided() {
		return includeVoided;
	}
	
	public void setIncludeVoided(boolean includeVoided) {
		this.includeVoided = includeVoided;
	}
	
	public Integer getLimit() {
		return limit;
	}
	
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	
	public Integer getStartIndex() {
		return startIndex;
	}
	
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	
	public Boolean getIsDrug() {
		return isDrug;
	}
	
	public void setIsDrug(Boolean isDrug) {
		this.isDrug = isDrug;
	}
	
	public List<Integer> getEncounterIds() {
		return encounterIds;
	}
	
	public void setEncounterIds(List<Integer> encounterIds) {
		this.encounterIds = encounterIds;
	}
	
	public List<String> getEncounterUuids() {
		return encounterUuids;
	}
	
	public void setEncounterUuids(List<String> encounterUuids) {
		this.encounterUuids = encounterUuids;
	}
	
	public Date getOrderDateMin() {
		return orderDateMin;
	}
	
	public void setOrderDateMin(Date orderDateMin) {
		this.orderDateMin = orderDateMin;
	}
	
	public Date getOrderDateMax() {
		return orderDateMax;
	}
	
	public void setOrderDateMax(Date orderDateMax) {
		this.orderDateMax = orderDateMax;
	}
}
