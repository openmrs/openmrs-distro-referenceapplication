package org.openmrs.module.stockmanagement.api.dto;

public class RecordPermission {
	
	private boolean canEdit;
	
	private boolean canView;
	
	private boolean canApprove;
	
	private boolean canReceipt;
	
	private boolean displayReceipt;
	
	public boolean getCanEdit() {
		return canEdit;
	}
	
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	
	public boolean getCanView() {
		return canView;
	}
	
	public void setCanView(boolean canView) {
		this.canView = canView;
	}
	
	public boolean getCanApprove() {
		return canApprove;
	}
	
	public void setCanApprove(boolean canApprove) {
		this.canApprove = canApprove;
	}
	
	public boolean getCanReceipt() {
		return canReceipt;
	}
	
	public void setCanReceipt(boolean canReceipt) {
		this.canReceipt = canReceipt;
	}
	
	public boolean getDisplayReceipt() {
		return displayReceipt;
	}
	
	public void setDisplayReceipt(boolean displayReceipt) {
		this.displayReceipt = displayReceipt;
	}
}
