package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.module.stockmanagement.api.model.StockOperationStatus;

public class StockOperationLinkDTO {
	
	private int id;
	
	private String uuid;
	
	private String parentUuid;
	
	private String parentOperationNumber;
	
	private String parentOperationTypeName;
	
	private StockOperationStatus parentStatus;
	
	private boolean parentVoided;
	
	private String childUuid;
	
	private String childOperationNumber;
	
	private String childOperationTypeName;
	
	private StockOperationStatus childStatus;
	
	private boolean childVoided;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getChildUuid() {
		return childUuid;
	}
	
	public void setChildUuid(String childUuid) {
		this.childUuid = childUuid;
	}
	
	public String getChildOperationNumber() {
		return childOperationNumber;
	}
	
	public void setChildOperationNumber(String childOperationNumber) {
		this.childOperationNumber = childOperationNumber;
	}
	
	public StockOperationStatus getChildStatus() {
		return childStatus;
	}
	
	public void setChildStatus(StockOperationStatus childStatus) {
		this.childStatus = childStatus;
	}
	
	public StockOperationStatus getParentStatus() {
		return parentStatus;
	}
	
	public void setParentStatus(StockOperationStatus parentStatus) {
		this.parentStatus = parentStatus;
	}
	
	public boolean getChildVoided() {
		return childVoided;
	}
	
	public void setChildVoided(boolean childVoided) {
		this.childVoided = childVoided;
	}
	
	public String getParentUuid() {
		return parentUuid;
	}
	
	public void setParentUuid(String parentUuid) {
		this.parentUuid = parentUuid;
	}
	
	public String getParentOperationNumber() {
		return parentOperationNumber;
	}
	
	public void setParentOperationNumber(String parentOperationNumber) {
		this.parentOperationNumber = parentOperationNumber;
	}
	
	public boolean getParentVoided() {
		return parentVoided;
	}
	
	public void setParentVoided(boolean parentVoided) {
		this.parentVoided = parentVoided;
	}
	
	public String getParentOperationTypeName() {
		return parentOperationTypeName;
	}
	
	public void setParentOperationTypeName(String parentOperationTypeName) {
		this.parentOperationTypeName = parentOperationTypeName;
	}
	
	public String getChildOperationTypeName() {
		return childOperationTypeName;
	}
	
	public void setChildOperationTypeName(String childOperationTypeName) {
		this.childOperationTypeName = childOperationTypeName;
	}
}
