package org.openmrs.module.stockmanagement.api.model;

import org.openmrs.*;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * The persistent class for the stockmgmt_stock_item_transaction database table.
 */
@Entity(name = "stockmanagement.StockItemTransaction")
@Table(name = "stockmgmt_stock_item_transaction")
public class StockItemTransaction extends TransactionBase implements Serializable {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = true)
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id", nullable = true)
	private Encounter encounter;
	
	public StockItemTransaction() {
	}
	
	public StockItemTransaction(TransactionBase tx) {
		super(tx);
	}
	
	public StockItemTransaction(StockOperation stockOperation, StockOperationItem item) {
		super(stockOperation, item);
	}
	
	public Order getOrder() {
		return order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public Encounter getEncounter() {
		return encounter;
	}
	
	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}
}
