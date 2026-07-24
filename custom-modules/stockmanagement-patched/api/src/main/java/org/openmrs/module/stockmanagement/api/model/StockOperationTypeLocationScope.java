package org.openmrs.module.stockmanagement.api.model;

import org.openmrs.Location;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the stockmgmt_user_role_scope_location database table.
 */
@Entity(name = "stockmanagement.StockOperationTypeLocationScope")
@Table(name = "stockmgmt_operation_type_location_scope")
public class StockOperationTypeLocationScope extends org.openmrs.BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_operation_type_location_scope_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_operation_type_id")
	private StockOperationType stockOperationType;
	
	@Column(name = "location_tag", nullable = false, length = 50)
	private String locationTag;
	
	@Column(name = "is_source", nullable = false)
	private boolean isSource;
	
	@Column(name = "is_destination", nullable = false)
	private boolean isDestination;
	
	public StockOperationTypeLocationScope() {
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public StockOperationType getStockOperationType() {
		return stockOperationType;
	}
	
	public void setStockOperationType(StockOperationType stockOperationType) {
		this.stockOperationType = stockOperationType;
	}
	
	public String getLocationTag() {
		return locationTag;
	}
	
	public void setLocationTag(String locationTag) {
		this.locationTag = locationTag;
	}
	
	public boolean getIsSource() {
		return isSource;
	}
	
	public void setIsSource(boolean isSource) {
		this.isSource = isSource;
	}
	
	public boolean getIsDestination() {
		return isDestination;
	}
	
	public void setIsDestination(boolean isDestination) {
		this.isDestination = isDestination;
	}
}
