package org.openmrs.module.stockmanagement.api.model;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the stockmgmt_stock_operation_item database table.
 */
@Entity(name = "stockmanagement.StockOperationItem")
@Table(name = "stockmgmt_stock_operation_item")
public class StockOperationItem extends org.openmrs.BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_operation_item_id")
	private Integer id;
	
	@Column(name = "quantity", nullable = true)
	private BigDecimal quantity;
	
	@Column(name = "purchase_price", nullable = true)
	private BigDecimal purchasePrice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_batch_id")
	private StockBatch stockBatch;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_item_packaging_uom_id")
	private StockItemPackagingUOM stockItemPackagingUOM;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_item_id")
	private StockItem stockItem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_operation_id")
	private StockOperation stockOperation;
	
	@Column(name = "quantity_received", nullable = true)
	private BigDecimal quantityReceived;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qty_rcvd_packaging_uom_id")
	private StockItemPackagingUOM quantityReceivedPackagingUOM;
	
	@Column(name = "quantity_requested", nullable = true)
	private BigDecimal quantityRequested;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qty_req_packaging_uom_id")
	private StockItemPackagingUOM quantityRequestedPackagingUOM;
	
	public StockOperationItem() {
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public BigDecimal getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	
	public StockBatch getStockBatch() {
		return this.stockBatch;
	}
	
	public void setStockBatch(StockBatch stockBatch) {
		this.stockBatch = stockBatch;
	}
	
	public StockItemPackagingUOM getStockItemPackagingUOM() {
		return this.stockItemPackagingUOM;
	}
	
	public void setStockItemPackagingUOM(StockItemPackagingUOM stockItemPackagingUOM) {
		this.stockItemPackagingUOM = stockItemPackagingUOM;
	}
	
	public StockItem getStockItem() {
		return this.stockItem;
	}
	
	public void setStockItem(StockItem stockItem) {
		this.stockItem = stockItem;
	}
	
	public StockOperation getStockOperation() {
		return this.stockOperation;
	}
	
	public void setStockOperation(StockOperation stockOperation) {
		this.stockOperation = stockOperation;
	}
	
	public BigDecimal getQuantityReceived() {
		return quantityReceived;
	}
	
	public void setQuantityReceived(BigDecimal quantityReceived) {
		this.quantityReceived = quantityReceived;
	}
	
	public StockItemPackagingUOM getQuantityReceivedPackagingUOM() {
		return quantityReceivedPackagingUOM;
	}
	
	public void setQuantityReceivedPackagingUOM(StockItemPackagingUOM quantityReceivedPackagingUOM) {
		this.quantityReceivedPackagingUOM = quantityReceivedPackagingUOM;
	}
	
	public BigDecimal getQuantityRequested() {
		return quantityRequested;
	}
	
	public void setQuantityRequested(BigDecimal quantityRequested) {
		this.quantityRequested = quantityRequested;
	}
	
	public StockItemPackagingUOM getQuantityRequestedPackagingUOM() {
		return quantityRequestedPackagingUOM;
	}
	
	public void setQuantityRequestedPackagingUOM(StockItemPackagingUOM quantityRequestedPackagingUOM) {
		this.quantityRequestedPackagingUOM = quantityRequestedPackagingUOM;
	}
}
