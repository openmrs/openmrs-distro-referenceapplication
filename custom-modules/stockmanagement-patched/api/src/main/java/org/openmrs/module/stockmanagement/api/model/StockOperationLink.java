package org.openmrs.module.stockmanagement.api.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity(name = "stockmanagement.StockOperationLink")
@Table(name = "stockmgmt_stock_operation_link")
public class StockOperationLink extends org.openmrs.BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_operation_link_id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_stock_operation_id")
	private StockOperation parent;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_stock_operation_id")
	private StockOperation child;
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public StockOperation getParent() {
		return parent;
	}
	
	public void setParent(StockOperation parent) {
		this.parent = parent;
	}
	
	public StockOperation getChild() {
		return child;
	}
	
	public void setChild(StockOperation child) {
		this.child = child;
	}
}
