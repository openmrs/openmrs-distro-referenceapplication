package org.openmrs.module.stockmanagement.api.dto.reporting;

import java.util.Date;

public class ReportFilter {
	
	private Date startDate;
	
	private Date endDate;
	
	private Integer atLocationId;
	
	private Boolean childLocations;
	
	private int startIndex;
	
	private int limit;
	
	private Integer stockItemCategoryConceptId;
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Integer getAtLocationId() {
		return atLocationId;
	}
	
	public void setAtLocationId(Integer atLocationId) {
		this.atLocationId = atLocationId;
	}
	
	public Boolean getChildLocations() {
		return childLocations;
	}
	
	public void setChildLocations(Boolean childLocations) {
		this.childLocations = childLocations;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public Integer getStockItemCategoryConceptId() {
		return stockItemCategoryConceptId;
	}
	
	public void setStockItemCategoryConceptId(Integer stockItemCategoryConceptId) {
		this.stockItemCategoryConceptId = stockItemCategoryConceptId;
	}
}
