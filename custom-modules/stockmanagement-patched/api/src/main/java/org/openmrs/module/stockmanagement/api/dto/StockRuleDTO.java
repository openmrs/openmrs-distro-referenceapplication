package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.Location;
import org.openmrs.module.stockmanagement.api.model.StockItem;
import org.openmrs.module.stockmanagement.api.model.StockItemPackagingUOM;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

public class StockRuleDTO {
	
	private Integer id;
	
	private String uuid;
	
	private Integer stockItemId;
	
	private String stockItemUuid;
	
	private String name;
	
	private String description;
	
	private Integer locationId;
	
	private String locationUuid;
	
	private String locationName;
	
	private BigDecimal quantity;
	
	private Integer stockItemPackagingUOMId;
	
	private String stockItemPackagingUOMUuid;
	
	private Integer packagingUoMId;
	
	private String packagingUomName;
	
	private Boolean enabled;
	
	private Long evaluationFrequency;
	
	private Date lastEvaluation;
	
	private Date nextEvaluation;
	
	private Long actionFrequency;
	
	private Date lastActionDate;
	
	private String alertRole;
	
	private String mailRole;
	
	private Integer creator;
	
	private Date dateCreated;
	
	private String creatorGivenName;
	
	private String creatorFamilyName;
	
	private boolean voided;
	
	private Boolean enableDescendants;
	
	private Date nextActionDate;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Integer getStockItemId() {
		return stockItemId;
	}
	
	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}
	
	public String getStockItemUuid() {
		return stockItemUuid;
	}
	
	public void setStockItemUuid(String stockItemUuid) {
		this.stockItemUuid = stockItemUuid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getLocationId() {
		return locationId;
	}
	
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}
	
	public String getLocationUuid() {
		return locationUuid;
	}
	
	public void setLocationUuid(String locationUuid) {
		this.locationUuid = locationUuid;
	}
	
	public String getLocationName() {
		return locationName;
	}
	
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public Integer getStockItemPackagingUOMId() {
		return stockItemPackagingUOMId;
	}
	
	public void setStockItemPackagingUOMId(Integer stockItemPackagingUOMId) {
		this.stockItemPackagingUOMId = stockItemPackagingUOMId;
	}
	
	public String getStockItemPackagingUOMUuid() {
		return stockItemPackagingUOMUuid;
	}
	
	public void setStockItemPackagingUOMUuid(String stockItemPackagingUOMUuid) {
		this.stockItemPackagingUOMUuid = stockItemPackagingUOMUuid;
	}
	
	public Integer getPackagingUoMId() {
		return packagingUoMId;
	}
	
	public void setPackagingUoMId(Integer packagingUoMId) {
		this.packagingUoMId = packagingUoMId;
	}
	
	public String getPackagingUomName() {
		return packagingUomName;
	}
	
	public void setPackagingUomName(String packagingUomName) {
		this.packagingUomName = packagingUomName;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public Long getEvaluationFrequency() {
		return evaluationFrequency;
	}
	
	public void setEvaluationFrequency(Long evaluationFrequency) {
		this.evaluationFrequency = evaluationFrequency;
	}
	
	public Date getLastEvaluation() {
		return lastEvaluation;
	}
	
	public void setLastEvaluation(Date lastEvaluation) {
		this.lastEvaluation = lastEvaluation;
	}
	
	public Date getNextEvaluation() {
		return nextEvaluation;
	}
	
	public void setNextEvaluation(Date nextEvaluation) {
		this.nextEvaluation = nextEvaluation;
	}
	
	public Long getActionFrequency() {
		return actionFrequency;
	}
	
	public void setActionFrequency(Long actionFrequency) {
		this.actionFrequency = actionFrequency;
	}
	
	public Date getLastActionDate() {
		return lastActionDate;
	}
	
	public void setLastActionDate(Date lastActionDate) {
		this.lastActionDate = lastActionDate;
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
	
	public Integer getCreator() {
		return creator;
	}
	
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
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
	
	public boolean getVoided() {
		return voided;
	}
	
	public void setVoided(boolean voided) {
		this.voided = voided;
	}
	
	public Boolean getEnableDescendants() {
		return enableDescendants;
	}
	
	public void setEnableDescendants(Boolean enableDescendants) {
		this.enableDescendants = enableDescendants;
	}
	
	public Date getNextActionDate() {
		return nextActionDate;
	}
	
	public void setNextActionDate(Date nextActionDate) {
		this.nextActionDate = nextActionDate;
	}
}
