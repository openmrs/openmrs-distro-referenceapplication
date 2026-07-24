package org.openmrs.module.stockmanagement.api.dto;

import java.util.List;

public class ImportResult {
	
	private int createdCount = 0;
	
	private int updatedCount = 0;
	
	private int notChangedCount = 0;
	
	private String uploadSessionId;
	
	private boolean hasErrorFile = false;
	
	private boolean success;
	
	private List<String> errors;
	
	private Exception exception;
	
	public int getNotChangedCount() {
		return notChangedCount;
	}
	
	public void setNotChangedCount(int notChangedCount) {
		this.notChangedCount = notChangedCount;
	}
	
	public int getCreatedCount() {
		return createdCount;
	}
	
	public void setCreatedCount(int createdCount) {
		this.createdCount = createdCount;
	}
	
	public int getUpdatedCount() {
		return updatedCount;
	}
	
	public void setUpdatedCount(int updatedCount) {
		this.updatedCount = updatedCount;
	}
	
	public boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public List<String> getErrors() {
		return errors;
	}
	
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	public Exception getException() {
		return exception;
	}
	
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	public String getUploadSessionId() {
		return uploadSessionId;
	}
	
	public void setUploadSessionId(String uploadSessionId) {
		this.uploadSessionId = uploadSessionId;
	}
	
	public boolean getHasErrorFile() {
		return hasErrorFile;
	}
	
	public void setHasErrorFile(boolean hasErrorFile) {
		this.hasErrorFile = hasErrorFile;
	}
}
