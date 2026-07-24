package org.openmrs.module.stockmanagement.api.dto;

public class RecordPrivilegeFilter {
	
	private Integer locationId;
	
	private Integer operationTypeId;
	
	public Integer getLocationId() {
		return locationId;
	}
	
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	
	public Integer getOperationTypeId() {
		return operationTypeId;
	}
	
	public void setOperationTypeId(Integer operationTypeId) {
		this.operationTypeId = operationTypeId;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		
		RecordPrivilegeFilter that = (RecordPrivilegeFilter) o;
		
		if (locationId != null ? !locationId.equals(that.locationId) : that.locationId != null)
			return false;
		return !(operationTypeId != null ? !operationTypeId.equals(that.operationTypeId) : that.operationTypeId != null);
		
	}
	
	@Override
	public int hashCode() {
		int result = locationId != null ? locationId.hashCode() : 0;
		result = 31 * result + (operationTypeId != null ? operationTypeId.hashCode() : 0);
		return result;
	}
}
