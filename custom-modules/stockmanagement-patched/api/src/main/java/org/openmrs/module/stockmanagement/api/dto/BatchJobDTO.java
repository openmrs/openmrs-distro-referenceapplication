package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.BaseOpenmrsData;
import org.openmrs.User;
import org.openmrs.module.stockmanagement.api.model.BatchJobOwner;
import org.openmrs.module.stockmanagement.api.model.BatchJobStatus;
import org.openmrs.module.stockmanagement.api.model.BatchJobType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BatchJobDTO {
	
	private Integer id;
	
	private BatchJobType batchJobType;
	
	private BatchJobStatus status;
	
	private String description;
	
	private Date startTime;
	
	private Date endTime;
	
	private Date expiration;
	
	private String parameters;
	
	private String privilegeScope;
	
	private String locationScope;
	
	private Integer locationScopeId;
	
	private String locationScopeUuid;
	
	private String executionState;
	
	private String cancelReason;
	
	private Integer cancelledBy;
	
	private String cancelledByUuid;
	
	private String cancelledByGivenName;
	
	private String cancelledByFamilyName;
	
	private Date cancelledDate;
	
	private String exitMessage;
	
	private Date completedDate;
	
	private Date dateCreated;
	
	private Integer creator;
	
	private String creatorUuid;
	
	private String creatorGivenName;
	
	private String creatorFamilyName;
	
	private boolean voided;
	
	private Long outputArtifactSize;
	
	private List<BatchJobOwnerDTO> owners;
	
	private String uuid;
	
	private String outputArtifactFileExt;
	
	private Boolean outputArtifactViewable;
	
	public BatchJobDTO() {
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public BatchJobType getBatchJobType() {
		return batchJobType;
	}
	
	public void setBatchJobType(BatchJobType batchJobType) {
		this.batchJobType = batchJobType;
	}
	
	public BatchJobStatus getStatus() {
		return status;
	}
	
	public void setStatus(BatchJobStatus status) {
		this.status = status;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Date getExpiration() {
		return expiration;
	}
	
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
	public String getParameters() {
		return parameters;
	}
	
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	public String getExecutionState() {
		return executionState;
	}
	
	public void setExecutionState(String executionState) {
		this.executionState = executionState;
	}
	
	public String getCancelReason() {
		return cancelReason;
	}
	
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	
	public Integer getCancelledBy() {
		return cancelledBy;
	}
	
	public String getCancelledByGivenName() {
		return cancelledByGivenName;
	}
	
	public void setCancelledByGivenName(String cancelledByGivenName) {
		this.cancelledByGivenName = cancelledByGivenName;
	}
	
	public String getCancelledByFamilyName() {
		return cancelledByFamilyName;
	}
	
	public void setCancelledByFamilyName(String cancelledByFamilyName) {
		this.cancelledByFamilyName = cancelledByFamilyName;
	}
	
	public Date getCancelledDate() {
		return cancelledDate;
	}
	
	public void setCancelledDate(Date cancelledDate) {
		this.cancelledDate = cancelledDate;
	}
	
	public String getExitMessage() {
		return exitMessage;
	}
	
	public void setExitMessage(String exitMessage) {
		this.exitMessage = exitMessage;
	}
	
	public Date getCompletedDate() {
		return completedDate;
	}
	
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public Integer getCreator() {
		return creator;
	}
	
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	
	public String getCreatorGivenName() {
		return creatorGivenName;
	}
	
	public void setCreatorGivenName(String creatorGivenName) {
		this.creatorGivenName = creatorGivenName;
	}
	
	public String getCreatorFamilyName() {
		return creatorFamilyName;
	}
	
	public void setCreatorFamilyName(String creatorFamilyName) {
		this.creatorFamilyName = creatorFamilyName;
	}
	
	public boolean isVoided() {
		return voided;
	}
	
	public void setVoided(boolean voided) {
		this.voided = voided;
	}
	
	public Long getOutputArtifactSize() {
		return outputArtifactSize;
	}
	
	public void setOutputArtifactSize(Long outputArtifactSize) {
		this.outputArtifactSize = outputArtifactSize;
	}
	
	public List<BatchJobOwnerDTO> getOwners() {
		return owners;
	}
	
	public void setOwners(List<BatchJobOwnerDTO> owners) {
		this.owners = owners;
	}
	
	public String getPrivilegeScope() {
		return privilegeScope;
	}
	
	public void setPrivilegeScope(String privilegeScope) {
		this.privilegeScope = privilegeScope;
	}
	
	public String getLocationScope() {
		return locationScope;
	}
	
	public void setLocationScope(String locationScope) {
		this.locationScope = locationScope;
	}
	
	public Integer getLocationScopeId() {
		return locationScopeId;
	}
	
	public void setLocationScopeId(Integer locationScopeId) {
		this.locationScopeId = locationScopeId;
	}
	
	public void setCancelledBy(Integer cancelledBy) {
		this.cancelledBy = cancelledBy;
	}
	
	public String getCancelledByUuid() {
		return cancelledByUuid;
	}
	
	public void setCancelledByUuid(String cancelledByUuid) {
		this.cancelledByUuid = cancelledByUuid;
	}
	
	public String getCreatorUuid() {
		return creatorUuid;
	}
	
	public void setCreatorUuid(String creatorUuid) {
		this.creatorUuid = creatorUuid;
	}
	
	public String getLocationScopeUuid() {
		return locationScopeUuid;
	}
	
	public void setLocationScopeUuid(String locationScopeUuid) {
		this.locationScopeUuid = locationScopeUuid;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getOutputArtifactFileExt() {
		return outputArtifactFileExt;
	}
	
	public void setOutputArtifactFileExt(String outputArtifactFileExt) {
		this.outputArtifactFileExt = outputArtifactFileExt;
	}
	
	public Boolean getOutputArtifactViewable() {
		return outputArtifactViewable;
	}
	
	public void setOutputArtifactViewable(Boolean outputArtifactViewable) {
		this.outputArtifactViewable = outputArtifactViewable;
	}
}
