package org.openmrs.module.stockmanagement.api.dto;

public class UserRoleScopeLocationDTO {
	
	private String uuid;
	
	private Integer userRoleScopeId;
	
	private String locationUuid;
	
	private String locationName;
	
	private Boolean enableDescendants;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Integer getUserRoleScopeId() {
		return userRoleScopeId;
	}
	
	public void setUserRoleScopeId(Integer userRoleScopeId) {
		this.userRoleScopeId = userRoleScopeId;
	}
	
	public String getLocationUuid() {
		return locationUuid;
	}
	
	public void setLocationUuid(String locationUuid) {
		this.locationUuid = locationUuid;
	}
	
	public String getLocationName() {
		return locationName;
	}
	
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	public Boolean getEnableDescendants() {
		return enableDescendants;
	}
	
	public void setEnableDescendants(Boolean enableDescendants) {
		this.enableDescendants = enableDescendants;
	}
}
