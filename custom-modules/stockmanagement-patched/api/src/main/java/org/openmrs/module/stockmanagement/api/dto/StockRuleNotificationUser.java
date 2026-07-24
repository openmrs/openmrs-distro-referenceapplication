package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.notification.Alert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class StockRuleNotificationUser {
	
	private Integer id;
	
	private Integer stockItemId;
	
	private Integer locationId;
	
	private BigDecimal quantity;
	
	private BigDecimal factor;
	
	private Integer packagingConceptId;
	
	private Boolean enableDescendants;
	
	private List<Integer> alertUserIds;
	
	private List<Integer> mailUserIds;
	
	private String alertRole;
	
	private String mailRole;
	
	private Integer alertRoleLocationHashCode;
	
	private Integer mailRoleLocationHashCode;
	
	private Integer stockItemLocationEnableDescendantsHashCode;
	
	private Long evaluationFrequency;
	
	private Long actionFrequency;
	
	private Alert alert;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
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
	
	public Integer getPackagingConceptId() {
		return packagingConceptId;
	}
	
	public void setPackagingConceptId(Integer packagingConceptId) {
		this.packagingConceptId = packagingConceptId;
	}
	
	public Boolean getEnableDescendants() {
		return enableDescendants;
	}
	
	public void setEnableDescendants(Boolean enableDescendants) {
		this.enableDescendants = enableDescendants;
	}
	
	public List<Integer> getAlertUserIds() {
		return alertUserIds;
	}
	
	public void setAlertUserIds(List<Integer> alertUserIds) {
		this.alertUserIds = alertUserIds;
	}
	
	public List<Integer> getMailUserIds() {
		return mailUserIds;
	}
	
	public void setMailUserIds(List<Integer> mailUserIds) {
		this.mailUserIds = mailUserIds;
	}
	
	public String getAlertRole() {
		return alertRole;
	}
	
	public void setAlertRole(String alertRole) {
		this.alertRole = alertRole;
	}
	
	public String getMailRole() {
		return mailRole;
	}
	
	public void setMailRole(String mailRole) {
		this.mailRole = mailRole;
	}
	
	public BigDecimal getFactor() {
		return factor;
	}
	
	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}
	
	public int getStockItemLocationEnableDescendantsHashCode() {
		if (stockItemLocationEnableDescendantsHashCode == null) {
			int result = stockItemId.hashCode();
			result = 31 * result + locationId.hashCode();
			result = 31 * result + enableDescendants.hashCode();
			stockItemLocationEnableDescendantsHashCode = result;
		}
		return stockItemLocationEnableDescendantsHashCode;
	}
	
	public Integer getAlertRoleLocationHashCode() {
		if (alertRoleLocationHashCode == null) {
			int result = locationId != null ? locationId.hashCode() : 0;
			result = 31 * result + (alertRole != null ? alertRole.hashCode() : 0);
			alertRoleLocationHashCode = result;
		}
		return alertRoleLocationHashCode;
	}
	
	public Integer getMailRoleLocationHashCode() {
		if (mailRoleLocationHashCode == null) {
			int result = locationId != null ? locationId.hashCode() : 0;
			result = 31 * result + (alertRole != null ? alertRole.hashCode() : 0);
			mailRoleLocationHashCode = result;
		}
		return mailRoleLocationHashCode;
	}
	
	public Long getEvaluationFrequency() {
		return evaluationFrequency;
	}
	
	public void setEvaluationFrequency(Long evaluationFrequency) {
		this.evaluationFrequency = evaluationFrequency;
	}
	
	public Long getActionFrequency() {
		return actionFrequency;
	}
	
	public void setActionFrequency(Long actionFrequency) {
		this.actionFrequency = actionFrequency;
	}
	
	public Alert getAlert() {
		return alert;
	}
	
	public void setAlert(Alert alert) {
		this.alert = alert;
	}
}
