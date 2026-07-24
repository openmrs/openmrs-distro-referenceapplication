package org.openmrs.module.stockmanagement.api.dto;

import java.math.BigDecimal;

public class StockRuleCurrentQuantity {
	
	private Integer stockItemId;
	
	private Integer locationId;
	
	private BigDecimal quantity;
	
	private Boolean enableDescendants;
	
	public StockRuleCurrentQuantity() {
	}
	
	public StockRuleCurrentQuantity(Integer stockItemId, Integer locationId, BigDecimal quantity, Boolean enableDescendants) {
		this.stockItemId = stockItemId;
		this.locationId = locationId;
		this.quantity = quantity;
		this.enableDescendants = enableDescendants;
	}
	
	public Integer getStockItemId() {
		return stockItemId;
	}
	
	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}
	
	public Integer getLocationId() {
		return locationId;
	}
	
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public Boolean getEnableDescendants() {
		return enableDescendants;
	}
	
	public void setEnableDescendants(Boolean enableDescendants) {
		this.enableDescendants = enableDescendants;
	}
	
	public int getStockItemLocationEnableDescendantsHashCode() {
		int result = stockItemId.hashCode();
		result = 31 * result + locationId.hashCode();
		result = 31 * result + enableDescendants.hashCode();
		return result;
	}
}
