package org.openmrs.module.stockmanagement.api.dto;

import java.util.Date;

public class BatchJobOwnerDTO {
	
	private Integer id;
	
	private Integer batchJobId;
	
	private String batchJobUuid;
	
	private Integer ownerUserId;
	
	private String ownerUserUuid;
	
	private String ownerGivenName;
	
	private String ownerFamilyName;
	
	private Date dateCreated;
	
	private String uuid;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getBatchJobId() {
		return batchJobId;
	}
	
	public void setBatchJobId(Integer batchJobId) {
		this.batchJobId = batchJobId;
	}
	
	public Integer getOwnerUserId() {
		return ownerUserId;
	}
	
	public void setOwnerUserId(Integer ownerUserId) {
		this.ownerUserId = ownerUserId;
	}
	
	public String getOwnerUserUuid() {
		return ownerUserUuid;
	}
	
	public void setOwnerUserUuid(String ownerUserUuid) {
		this.ownerUserUuid = ownerUserUuid;
	}
	
	public String getOwnerGivenName() {
		return ownerGivenName;
	}
	
	public void setOwnerGivenName(String ownerGivenName) {
		this.ownerGivenName = ownerGivenName;
	}
	
	public String getOwnerFamilyName() {
		return ownerFamilyName;
	}
	
	public void setOwnerFamilyName(String ownerFamilyName) {
		this.ownerFamilyName = ownerFamilyName;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getBatchJobUuid() {
		return batchJobUuid;
	}
	
	public void setBatchJobUuid(String batchJobUuid) {
		this.batchJobUuid = batchJobUuid;
	}
}
