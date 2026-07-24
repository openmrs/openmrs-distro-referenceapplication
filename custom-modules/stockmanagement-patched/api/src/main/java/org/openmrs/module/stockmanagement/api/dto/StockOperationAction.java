package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.api.context.Context;

import java.util.List;

public class StockOperationAction {
	
	public enum Action {
		SUBMIT(), APPROVE(), DISPATCH(), RETURN(), REJECT(), COMPLETE(), CANCEL(), QUANTITY_RECEIVED()
	}
	
	private String reason;
	
	private Action name;
	
	private String uuid;
	
	private List<StockOperationActionLineItem> lineItems;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Action getName() {
		return name;
	}
	
	public void setName(Action name) {
		this.name = name;
	}
	
	public List<StockOperationActionLineItem> getLineItems() {
		return lineItems;
	}
	
	public void setLineItems(List<StockOperationActionLineItem> lineItems) {
		this.lineItems = lineItems;
	}
	
	public static String getActionName(Action action) {
		switch (action) {
			case SUBMIT:
				return Context.getMessageSourceService().getMessage("stockmanagement.stockoperation.action.submitted");
			case APPROVE:
				return Context.getMessageSourceService().getMessage("stockmanagement.stockoperation.action.approved");
			case DISPATCH:
				return Context.getMessageSourceService().getMessage("stockmanagement.stockoperation.action.dispatched");
			case RETURN:
				return Context.getMessageSourceService().getMessage("stockmanagement.stockoperation.action.areturned");
			case REJECT:
				return Context.getMessageSourceService().getMessage("stockmanagement.stockoperation.action.rejected");
			case COMPLETE:
				return Context.getMessageSourceService().getMessage("stockmanagement.stockoperation.action.completed");
			case CANCEL:
				return Context.getMessageSourceService().getMessage("stockmanagement.stockoperation.action.cancelled");
			case QUANTITY_RECEIVED:
				return Context.getMessageSourceService()
				        .getMessage("stockmanagement.stockoperation.action.quantityreceived");
		}
		return "UNKNOWN";
	}
}
