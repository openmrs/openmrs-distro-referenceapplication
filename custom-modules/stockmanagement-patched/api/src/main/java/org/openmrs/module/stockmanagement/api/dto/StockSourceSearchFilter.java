package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.Concept;
import org.openmrs.Drug;

import java.util.List;

public class StockSourceSearchFilter {
	
	private String uuid;
	
	private Integer startIndex;
	
	private Integer limit;
	
	private Concept sourceType;
	
	private boolean includeVoided;
	
	private String textSearch;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	
	public Concept getSourceType() {
		return sourceType;
	}
	
	public void setSourceType(Concept sourceType) {
		this.sourceType = sourceType;
	}
	
	public boolean getIncludeVoided() {
		return includeVoided;
	}
	
	public void setIncludeVoided(boolean includeVoided) {
		this.includeVoided = includeVoided;
	}
	
	public String getTextSearch() {
		return textSearch;
	}
	
	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
	}
}
