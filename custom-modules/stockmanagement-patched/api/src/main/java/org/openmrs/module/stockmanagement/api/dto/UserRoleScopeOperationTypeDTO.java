package org.openmrs.module.stockmanagement.api.dto;

public class UserRoleScopeOperationTypeDTO {
	
	private Integer userRoleScopeId;
	
	private String uuid;
	
	private String operationTypeName;
	
	private String operationTypeUuid;
	
	public Integer getUserRoleScopeId() {
		return userRoleScopeId;
	}
	
	public void setUserRoleScopeId(Integer userRoleScopeId) {
		this.userRoleScopeId = userRoleScopeId;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getOperationTypeName() {
		return operationTypeName;
	}
	
	public void setOperationTypeName(String operationTypeName) {
		this.operationTypeName = operationTypeName;
	}
	
	public String getOperationTypeUuid() {
		return operationTypeUuid;
	}
	
	public void setOperationTypeUuid(String operationTypeUuid) {
		this.operationTypeUuid = operationTypeUuid;
	}
}
