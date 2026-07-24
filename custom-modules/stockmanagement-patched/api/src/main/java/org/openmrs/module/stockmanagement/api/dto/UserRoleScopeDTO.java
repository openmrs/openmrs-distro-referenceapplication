package org.openmrs.module.stockmanagement.api.dto;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class UserRoleScopeDTO {
	
	private String uuid;
	
	private int id;
	
	private String userUuid;
	
	private String userName;
	
	private String userGivenName;
	
	private String userFamilyName;
	
	private String role;
	
	private Boolean permanent;
	
	private Date activeFrom;
	
	private Date activeTo;
	
	private Boolean enabled;
	
	private List<UserRoleScopeLocationDTO> locations;
	
	private List<UserRoleScopeOperationTypeDTO> operationTypes;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUserUuid() {
		return userUuid;
	}
	
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserGivenName() {
		return userGivenName;
	}
	
	public void setUserGivenName(String userGivenName) {
		this.userGivenName = userGivenName;
	}
	
	public String getUserFamilyName() {
		return userFamilyName;
	}
	
	public void setUserFamilyName(String userFamilyName) {
		this.userFamilyName = userFamilyName;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public Boolean getPermanent() {
		return permanent;
	}
	
	public void setPermanent(Boolean permanent) {
		this.permanent = permanent;
	}
	
	public Date getActiveFrom() {
		return activeFrom;
	}
	
	public void setActiveFrom(Date activeFrom) {
		this.activeFrom = activeFrom;
	}
	
	public Date getActiveTo() {
		return activeTo;
	}
	
	public void setActiveTo(Date activeTo) {
		this.activeTo = activeTo;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public List<UserRoleScopeLocationDTO> getLocations() {
		return locations;
	}
	
	public void setLocations(List<UserRoleScopeLocationDTO> locations) {
		this.locations = locations;
	}
	
	public List<UserRoleScopeOperationTypeDTO> getOperationTypes() {
		return operationTypes;
	}
	
	public void setOperationTypes(List<UserRoleScopeOperationTypeDTO> operationTypes) {
		this.operationTypes = operationTypes;
	}
}
