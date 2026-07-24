package org.openmrs.module.stockmanagement.api.dto;

import java.util.List;

public class PartyDTO {
	
	private Integer id;
	
	private String uuid;
	
	private String name;
	
	private String acronym;
	
	private String locationUuid;
	
	private Integer locationId;
	
	private String stockSourceUuid;
	
	private Integer stockSourceId;
	
	private List<String> tags;
	
	private boolean voided;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAcronym() {
		return acronym;
	}
	
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	
	public String getLocationUuid() {
		return locationUuid;
	}
	
	public void setLocationUuid(String locationUuid) {
		this.locationUuid = locationUuid;
	}
	
	public String getStockSourceUuid() {
		return stockSourceUuid;
	}
	
	public void setStockSourceUuid(String stockSourceUuid) {
		this.stockSourceUuid = stockSourceUuid;
	}
	
	public List<String> getTags() {
		return tags;
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public boolean getVoided() {
		return voided;
	}
	
	public void setVoided(boolean voided) {
		this.voided = voided;
	}
	
	public Integer getLocationId() {
		return locationId;
	}
	
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	
	public Integer getStockSourceId() {
		return stockSourceId;
	}
	
	public void setStockSourceId(Integer stockSourceId) {
		this.stockSourceId = stockSourceId;
	}
}
