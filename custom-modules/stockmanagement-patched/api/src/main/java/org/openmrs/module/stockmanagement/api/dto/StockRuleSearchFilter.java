package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.module.stockmanagement.api.model.StockItem;

import java.util.Date;
import java.util.List;

public class StockRuleSearchFilter {
	
	private Integer id;
	
	private List<String> uuids;
	
	private Integer startIndex;
	
	private Integer limit;
	
	private List<String> stockItemUuids;
	
	private List<String> locationUuids;
	
	private Date lastEvaluationMin;
	
	private Date lastEvaluationMax;
	
	private Date nextEvaluationMin;
	
	private Date nextEvaluationMax;
	
	private Date lastActionDateMin;
	
	private Date lastActionDateMax;
	
	private Boolean hasNotificationRoleSet;
	
	private Boolean enabled;
	
	private boolean IncludeVoided = false;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public List<String> getUuids() {
		return uuids;
	}
	
	public void setUuids(List<String> uuids) {
		this.uuids = uuids;
	}
	
	public Integer getStartIndex() {
		return startIndex;
	}
	
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	
	public Integer getLimit() {
		return limit;
	}
	
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	
	public List<String> getStockItemUuids() {
		return stockItemUuids;
	}
	
	public void setStockItemUuids(List<String> stockItemUuids) {
		this.stockItemUuids = stockItemUuids;
	}
	
	public List<String> getLocationUuids() {
		return locationUuids;
	}
	
	public void setLocationUuids(List<String> locationUuids) {
		this.locationUuids = locationUuids;
	}
	
	public Date getLastEvaluationMin() {
		return lastEvaluationMin;
	}
	
	public void setLastEvaluationMin(Date lastEvaluationMin) {
		this.lastEvaluationMin = lastEvaluationMin;
	}
	
	public Date getLastEvaluationMax() {
		return lastEvaluationMax;
	}
	
	public void setLastEvaluationMax(Date lastEvaluationMax) {
		this.lastEvaluationMax = lastEvaluationMax;
	}
	
	public Date getNextEvaluationMin() {
		return nextEvaluationMin;
	}
	
	public void setNextEvaluationMin(Date nextEvaluationMin) {
		this.nextEvaluationMin = nextEvaluationMin;
	}
	
	public Date getNextEvaluationMax() {
		return nextEvaluationMax;
	}
	
	public void setNextEvaluationMax(Date nextEvaluationMax) {
		this.nextEvaluationMax = nextEvaluationMax;
	}
	
	public Date getLastActionDateMin() {
		return lastActionDateMin;
	}
	
	public void setLastActionDateMin(Date lastActionDateMin) {
		this.lastActionDateMin = lastActionDateMin;
	}
	
	public Date getLastActionDateMax() {
		return lastActionDateMax;
	}
	
	public void setLastActionDateMax(Date lastActionDateMax) {
		this.lastActionDateMax = lastActionDateMax;
	}
	
	public Boolean getHasNotificationRoleSet() {
		return hasNotificationRoleSet;
	}
	
	public void setHasNotificationRoleSet(Boolean hasNotificationRoleSet) {
		this.hasNotificationRoleSet = hasNotificationRoleSet;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean getIncludeVoided() {
		return IncludeVoided;
	}
	
	public void setIncludeVoided(boolean includeVoided) {
		IncludeVoided = includeVoided;
	}
}
