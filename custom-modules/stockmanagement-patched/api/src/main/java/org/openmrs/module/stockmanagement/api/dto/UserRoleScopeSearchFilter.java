package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.Location;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.module.stockmanagement.api.model.StockOperationType;

import java.util.List;

public class UserRoleScopeSearchFilter {
	
	private String uuid;
	
	private String name;
	
	private List<User> users;
	
	private Integer startIndex;
	
	private Integer limit;
	
	private Location location;
	
	private StockOperationType operationType;
	
	private boolean includeVoided;
	
	public UserRoleScopeSearchFilter(String uuid) {
		this.uuid = uuid;
	}
	
	public UserRoleScopeSearchFilter() {
		includeVoided = false;
	}
	
	public boolean getIncludeVoided() {
		return includeVoided;
	}
	
	public void setIncludeVoided(boolean includeVoided) {
		this.includeVoided = includeVoided;
	}
	
	public StockOperationType getOperationType() {
		return operationType;
	}
	
	public void setOperationType(StockOperationType operationType) {
		this.operationType = operationType;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
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
	
	public List<User> getUsers() {
		return users;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public UserRoleScopeSearchFilter withIncludeVoided(boolean includeVoided) {
		setIncludeVoided(includeVoided);
		return this;
	}
}
