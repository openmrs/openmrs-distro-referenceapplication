package org.openmrs.module.stockmanagement.api.model;

import org.openmrs.Concept;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * The persistent class for the stockmgmt_stock_item_packaging_uom database table.
 */
@Entity(name = "stockmanagement.StockItemPackagingUOM")
@Table(name = "stockmgmt_stock_item_packaging_uom")
public class StockItemPackagingUOM extends org.openmrs.BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_item_packaging_uom_id")
	private Integer id;
	
	@Column(name = "factor")
	private BigDecimal factor;
	
	@JoinColumn(name = "packaging_uom_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Concept packagingUom;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_item_id")
	private StockItem stockItem;
	
	//	@OneToMany(mappedBy = "stockItemPackagingUOM")
	//	private Set<StockItemTransaction> stockItemTransactions;
	//
	//	@OneToMany(mappedBy = "stockItemPackagingUOM")
	//	private Set<StockOperationItem> stockOperationItems;
	
	public StockItemPackagingUOM() {
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public BigDecimal getFactor() {
		return this.factor;
	}
	
	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}
	
	public Concept getPackagingUom() {
		return this.packagingUom;
	}
	
	public void setPackagingUom(Concept packagingUom) {
		this.packagingUom = packagingUom;
	}
	
	public StockItem getStockItem() {
		return this.stockItem;
	}
	
	public void setStockItem(StockItem stockItem) {
		this.stockItem = stockItem;
	}
	
	//	public Set<StockItemTransaction> getStockItemTransactions() {
	//		return this.stockItemTransactions;
	//	}
	//
	//	public void setStockItemTransactions(Set<StockItemTransaction> stockItemTransactions) {
	//		this.stockItemTransactions = stockItemTransactions;
	//	}
	
	//	public StockItemTransaction addStockItemTransaction(StockItemTransaction stockItemTransaction) {
	//		getStockItemTransactions().add(stockItemTransaction);
	//		stockItemTransaction.setStockItemPackagingUOM(this);
	//
	//		return stockItemTransaction;
	//	}
	//
	//	public StockItemTransaction removeStockItemTransaction(StockItemTransaction stockItemTransaction) {
	//		getStockItemTransactions().remove(stockItemTransaction);
	//		stockItemTransaction.setStockItemPackagingUOM(null);
	//
	//		return stockItemTransaction;
	//	}
	//
	//	public Set<StockOperationItem> getStockOperationItems() {
	//		return this.stockOperationItems;
	//	}
	//
	//	public void setStockOperationItems(Set<StockOperationItem> stockOperationItems) {
	//		this.stockOperationItems = stockOperationItems;
	//	}
	//
	//	public StockOperationItem addStockOperationItem(StockOperationItem stockOperationItem) {
	//		getStockOperationItems().add(stockOperationItem);
	//		stockOperationItem.setStockItemPackagingUOM(this);
	//
	//		return stockOperationItem;
	//	}
	//
	//	public StockOperationItem removeStockOperationItem(StockOperationItem stockOperationItem) {
	//		getStockOperationItems().remove(stockOperationItem);
	//		stockOperationItem.setStockItemPackagingUOM(null);
	//
	//		return stockOperationItem;
	//	}
	
}
