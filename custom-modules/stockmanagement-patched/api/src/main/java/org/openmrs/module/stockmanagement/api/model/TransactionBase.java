/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.module.stockmanagement.api.model;

import java.math.BigDecimal;
import java.util.Date;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.User;

import javax.persistence.*;

/**
 * Base model class used by models that have transaction information.
 */
@MappedSuperclass
public abstract class TransactionBase extends BaseOpenmrsObject implements Comparable<TransactionBase> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_item_transaction_id")
	private Integer id;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "creator")
	protected User creator;
	
	@Column(name = "date_created", nullable = false)
	private Date dateCreated;
	
	@JoinColumn(name = "party_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Party party;
	
	@JoinColumn(name = "patient_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Patient patient;
	
	@Column(name = "quantity", nullable = false)
	private BigDecimal quantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_batch_id")
	private StockBatch stockBatch;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_item_id")
	private StockItem stockItem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_operation_id")
	private StockOperation stockOperation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_operation_item_id")
	private StockOperationItem stockOperationItem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_item_packaging_uom_id")
	private StockItemPackagingUOM stockItemPackagingUOM;
	
	protected TransactionBase() {
	}
	
	protected TransactionBase(TransactionBase tx) {
		stockOperation = tx.stockOperation;
		stockOperationItem = tx.stockOperationItem;
		stockItem = tx.stockItem;
		stockBatch = tx.stockBatch;
		quantity = tx.quantity;
		stockItemPackagingUOM = tx.stockItemPackagingUOM;
		
	}
	
	protected TransactionBase(StockOperation stockOperation, StockOperationItem item) {
		this.stockItem = item.getStockItem();
		this.stockOperationItem = item;
		this.stockOperation = stockOperation;
		this.stockBatch = item.getStockBatch();
		this.quantity = item.getQuantity();
		this.stockItemPackagingUOM = item.getStockItemPackagingUOM();
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public User getCreator() {
		return this.creator;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public Date getDateCreated() {
		return this.dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public Party getParty() {
		return party;
	}
	
	public void setParty(Party party) {
		this.party = party;
	}
	
	public Patient getPatient() {
		return this.patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public BigDecimal getQuantity() {
		return this.quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public StockBatch getStockBatch() {
		return this.stockBatch;
	}
	
	public void setStockBatch(StockBatch stockBatch) {
		this.stockBatch = stockBatch;
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
	
	public StockItemPackagingUOM getStockItemPackagingUOM() {
		return this.stockItemPackagingUOM;
	}
	
	public StockOperationItem getStockOperationItem() {
		return stockOperationItem;
	}
	
	public void setStockOperationItem(StockOperationItem stockOperationItem) {
		this.stockOperationItem = stockOperationItem;
	}
	
	public void setStockItemPackagingUOM(StockItemPackagingUOM stockItemPackagingUOM) {
		this.stockItemPackagingUOM = stockItemPackagingUOM;
	}
	
	@Override
	public int compareTo(TransactionBase o) {
		if (o == null) {
			return 1;
		}
		
		int result = 0;
		
		if (getId() != null && o.getId() != null) {
			result = getId().compareTo(o.getId());
		}
		
		if (result == 0) {
			result = getUuid().compareTo(o.getUuid());
		}
		
		return result;
	}
}
