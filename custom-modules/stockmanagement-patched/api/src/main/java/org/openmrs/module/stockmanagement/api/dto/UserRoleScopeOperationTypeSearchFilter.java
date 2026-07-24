package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.User;
import org.openmrs.module.stockmanagement.api.model.UserRoleScope;

import java.util.List;

public class UserRoleScopeOperationTypeSearchFilter {
	
	private String uuid;
	
	private List<UserRoleScope> userRoleScopes;
	
	private boolean includeVoided;
	
	private Integer startIndex;
	
	private Integer limit;
	
	public UserRoleScopeOperationTypeSearchFilter() {
		includeVoided = false;
	}
	
	public UserRoleScopeOperationTypeSearchFilter(List<UserRoleScope> userRoleScopes) {
		this.userRoleScopes = userRoleScopes;
		includeVoided = false;
	}
	
	public boolean getIncludeVoided() {
		return includeVoided;
	}
	
	public void setIncludeVoided(boolean includeVoided) {
		this.includeVoided = includeVoided;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public List<UserRoleScope> getUserRoleScopes() {
		return userRoleScopes;
	}
	
	public void setUserRoleScopes(List<UserRoleScope> userRoleScopes) {
		this.userRoleScopes = userRoleScopes;
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
}
