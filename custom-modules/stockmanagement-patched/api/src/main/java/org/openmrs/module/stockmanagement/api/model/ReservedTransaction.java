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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Model class that represents individual item stock actions while an operation is pending.
 */
@Entity(name = "stockmanagement.ReservedTransaction")
@Table(name = "stockmgmt_reservation_transaction")
public class ReservedTransaction extends TransactionBase {
	
	@Column(name = "is_available")
	private Boolean isAvailable;
	
	public ReservedTransaction() {
	}
	
	public ReservedTransaction(TransactionBase tx) {
		super(tx);
	}
	
	public ReservedTransaction(StockOperation stockOperation, StockOperationItem item) {
		super(stockOperation, item);
	}
	
	public Boolean getIsAvailable() {
		return isAvailable;
	}
	
	public void setIsAvailable(Boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
}
