package org.openmrs.module.stockmanagement.api.dto;

import java.util.List;

public class PartySearchFilter {
	
	private String searchText;
	
	private Boolean includeVoided;
	
	private List<String> locationUuids;
	
	private List<Integer> locationIds;
	
	private List<Integer> partyIds;
	
	private List<String> partyUuids;
	
	private Integer startIndex;
	
	private Integer limit;
	
	public PartySearchFilter() {
		includeVoided = false;
	}
	
	public String getSearchText() {
		return searchText;
	}
	
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	public Boolean getIncludeVoided() {
		return includeVoided;
	}
	
	public void setIncludeVoided(Boolean includeVoided) {
		this.includeVoided = includeVoided;
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
	
	public List<String> getLocationUuids() {
		return locationUuids;
	}
	
	public void setLocationUuids(List<String> locationUuids) {
		this.locationUuids = locationUuids;
	}
	
	public List<Integer> getLocationIds() {
		return locationIds;
	}
	
	public void setLocationIds(List<Integer> locationIds) {
		this.locationIds = locationIds;
	}
	
	public List<Integer> getPartyIds() {
		return partyIds;
	}
	
	public void setPartyIds(List<Integer> partyIds) {
		this.partyIds = partyIds;
	}
	
	public List<String> getPartyUuids() {
		return partyUuids;
	}
	
	public void setPartyUuids(List<String> partyUuids) {
		this.partyUuids = partyUuids;
	}
}
