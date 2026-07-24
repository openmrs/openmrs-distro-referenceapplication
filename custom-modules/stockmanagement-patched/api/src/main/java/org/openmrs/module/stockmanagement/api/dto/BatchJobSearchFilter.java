package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.module.stockmanagement.api.model.BatchJobStatus;
import org.openmrs.module.stockmanagement.api.model.BatchJobType;

import java.util.Date;
import java.util.List;

public class BatchJobSearchFilter {
	
	private List<Integer> batchJobIds;
	
	private List<String> batchJobUuids;
	
	private BatchJobType batchJobType;
	
	private List<BatchJobStatus> batchJobStatus;
	
	private Date dateCreatedMin;
	
	private Date dateCreatedMax;
	
	private Date completedDateMin;
	
	private Date completedDateMax;
	
	private String parameters;
	
	private List<Integer> locationScopeIds;
	
	private String privilegeScope;
	
	private Integer startIndex;
	
	private Integer limit;
	
	private boolean includeVoided;
	
	private boolean returnOldestFirst;
	
	public List<String> getBatchJobUuids() {
		return batchJobUuids;
	}
	
	public void setBatchJobUuids(List<String> batchJobUuids) {
		this.batchJobUuids = batchJobUuids;
	}
	
	public BatchJobType getBatchJobType() {
		return batchJobType;
	}
	
	public void setBatchJobType(BatchJobType batchJobType) {
		this.batchJobType = batchJobType;
	}
	
	public List<BatchJobStatus> getBatchJobStatus() {
		return batchJobStatus;
	}
	
	public void setBatchJobStatus(List<BatchJobStatus> batchJobStatus) {
		this.batchJobStatus = batchJobStatus;
	}
	
	public Date getDateCreatedMin() {
		return dateCreatedMin;
	}
	
	public void setDateCreatedMin(Date dateCreatedMin) {
		this.dateCreatedMin = dateCreatedMin;
	}
	
	public Date getDateCreatedMax() {
		return dateCreatedMax;
	}
	
	public void setDateCreatedMax(Date dateCreatedMax) {
		this.dateCreatedMax = dateCreatedMax;
	}
	
	public Date getCompletedDateMin() {
		return completedDateMin;
	}
	
	public void setCompletedDateMin(Date completedDateMin) {
		this.completedDateMin = completedDateMin;
	}
	
	public Date getCompletedDateMax() {
		return completedDateMax;
	}
	
	public void setCompletedDateMax(Date completedDateMax) {
		this.completedDateMax = completedDateMax;
	}
	
	public String getParameters() {
		return parameters;
	}
	
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	public List<Integer> getLocationScopeIds() {
		return locationScopeIds;
	}
	
	public void setLocationScopeIds(List<Integer> locationScopeIds) {
		this.locationScopeIds = locationScopeIds;
	}
	
	public String getPrivilegeScope() {
		return privilegeScope;
	}
	
	public void setPrivilegeScope(String privilegeScope) {
		this.privilegeScope = privilegeScope;
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
	
	public List<Integer> getBatchJobIds() {
		return batchJobIds;
	}
	
	public void setBatchJobIds(List<Integer> batchJobIds) {
		this.batchJobIds = batchJobIds;
	}
	
	public boolean getIncludeVoided() {
		return includeVoided;
	}
	
	public void setIncludeVoided(boolean includeVoided) {
		this.includeVoided = includeVoided;
	}
}
