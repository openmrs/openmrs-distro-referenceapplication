package org.openmrs.module.stockmanagement.api.model;

import org.openmrs.Concept;
import org.openmrs.Location;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The persistent class for the stockmgmt_stock_rule database table.
 */
@Entity(name = "stockmanagement.StockRule")
@Table(name = "stockmgmt_stock_rule")
public class StockRule extends org.openmrs.BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_rule_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_item_id")
	private StockItem stockItem;
	
	@Column(name = "name", length = 255)
	private String name;
	
	@Column(name = "description", length = 500)
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Location location;
	
	@Column(name = "quantity", nullable = true)
	private BigDecimal quantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_item_packaging_uom_id")
	private StockItemPackagingUOM stockItemPackagingUOM;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@Column(name = "evaluation_frequency")
	private Long evaluationFrequency;
	
	@Column(name = "last_evaluation")
	private Date lastEvaluation;
	
	@Column(name = "next_evaluation")
	private Date nextEvaluation;
	
	@Column(name = "action_frequency")
	private Long actionFrequency;
	
	@Column(name = "last_action_date")
	private Date lastActionDate;
	
	@Column(name = "alert_role")
	private String alertRole;
	
	@Column(name = "mail_Role")
	private String mailRole;
	
	@Column(name = "enable_descendants")
	private Boolean enableDescendants;
	
	@Column(name = "next_action_date")
	private Date nextActionDate;
	
	public StockRule() {
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public StockItem getStockItem() {
		return this.stockItem;
	}
	
	public void setStockItem(StockItem stockItem) {
		this.stockItem = stockItem;
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
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public StockItemPackagingUOM getStockItemPackagingUOM() {
		return stockItemPackagingUOM;
	}
	
	public void setStockItemPackagingUOM(StockItemPackagingUOM stockItemPackagingUOM) {
		this.stockItemPackagingUOM = stockItemPackagingUOM;
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
	
	public Boolean getEnableDescendants() {
		return enableDescendants;
	}
	
	public void setEnableDescendants(Boolean enableDescendants) {
		this.enableDescendants = enableDescendants;
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
	
	public Date getNextActionDate() {
		return nextActionDate;
	}
	
	public void setNextActionDate(Date nextActionDate) {
		this.nextActionDate = nextActionDate;
	}
}
