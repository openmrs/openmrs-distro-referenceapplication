package org.openmrs.module.stockmanagement.api.dto;

import java.util.Date;

public class PrivilegeScope {
	
	private String role;
	
	private String privilege;
	
	private String operationTypeUuid;
	
	private String locationUuid;
	
	private String partyUuid;
	
	private boolean isPermanent;
	
	private Date activeFrom;
	
	private Date activeTo;
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getPrivilege() {
		return privilege;
	}
	
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	
	public String getOperationTypeUuid() {
		return operationTypeUuid;
	}
	
	public void setOperationTypeUuid(String operationTypeUuid) {
		this.operationTypeUuid = operationTypeUuid;
	}
	
	public String getLocationUuid() {
		return locationUuid;
	}
	
	public void setLocationUuid(String locationUuid) {
		this.locationUuid = locationUuid;
	}
	
	public String getPartyUuid() {
		return partyUuid;
	}
	
	public void setPartyUuid(String partyUuid) {
		this.partyUuid = partyUuid;
	}
	
	public boolean getIsPermanent() {
		return isPermanent;
	}
	
	public void setIsPermanent(boolean isPermanent) {
		this.isPermanent = isPermanent;
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
	
	public PrivilegeScope clone() {
		PrivilegeScope privilegeScope = new PrivilegeScope();
		privilegeScope.role = role;
		privilegeScope.privilege = privilege;
		privilegeScope.operationTypeUuid = operationTypeUuid;
		privilegeScope.locationUuid = locationUuid;
		privilegeScope.partyUuid = partyUuid;
		privilegeScope.isPermanent = isPermanent;
		privilegeScope.activeFrom = activeFrom;
		privilegeScope.activeTo = activeTo;
		return privilegeScope;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		
		PrivilegeScope that = (PrivilegeScope) o;
		
		if (isPermanent != that.isPermanent)
			return false;
		if (role != null ? !role.equals(that.role) : that.role != null)
			return false;
		if (privilege != null ? !privilege.equals(that.privilege) : that.privilege != null)
			return false;
		if (operationTypeUuid != null ? !operationTypeUuid.equals(that.operationTypeUuid) : that.operationTypeUuid != null)
			return false;
		if (locationUuid != null ? !locationUuid.equals(that.locationUuid) : that.locationUuid != null)
			return false;
		if (partyUuid != null ? !partyUuid.equals(that.partyUuid) : that.partyUuid != null)
			return false;
		if (activeFrom != null ? !activeFrom.equals(that.activeFrom) : that.activeFrom != null)
			return false;
		return !(activeTo != null ? !activeTo.equals(that.activeTo) : that.activeTo != null);
		
	}
	
	@Override
	public int hashCode() {
		int result = role != null ? role.hashCode() : 0;
		result = 31 * result + (privilege != null ? privilege.hashCode() : 0);
		result = 31 * result + (operationTypeUuid != null ? operationTypeUuid.hashCode() : 0);
		result = 31 * result + (locationUuid != null ? locationUuid.hashCode() : 0);
		result = 31 * result + (partyUuid != null ? partyUuid.hashCode() : 0);
		result = 31 * result + (isPermanent ? 1 : 0);
		result = 31 * result + (activeFrom != null ? activeFrom.hashCode() : 0);
		result = 31 * result + (activeTo != null ? activeTo.hashCode() : 0);
		return result;
	}
}
