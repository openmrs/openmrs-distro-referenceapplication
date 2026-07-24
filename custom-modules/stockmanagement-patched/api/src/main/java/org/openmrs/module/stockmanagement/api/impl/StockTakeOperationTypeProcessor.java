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
package org.openmrs.module.stockmanagement.api.impl;

import org.openmrs.module.stockmanagement.api.utils.Action2;
import org.openmrs.module.stockmanagement.api.model.ReservedTransaction;
import org.openmrs.module.stockmanagement.api.model.StockItemTransaction;
import org.openmrs.module.stockmanagement.api.model.StockOperation;
import org.openmrs.module.stockmanagement.api.model.StockOperationType;

import java.math.BigDecimal;

/**
 * Model class that represents an adjustment stock operation type. Adjustment operations directly
 * alter the item stock for a given stockroom and are used to correct item stock mistakes.
 */
public class StockTakeOperationTypeProcessor extends StockOperationTypeProcessorBase {
	
	public StockTakeOperationTypeProcessor(StockOperationType stockOperationType) {
		super(stockOperationType);
	}
	
	@Override
	public boolean requiresReason() {
		return true;
	}
	
	@Override
	public boolean isNegativeItemQuantityAllowed() {
		return false;
	}
	
	/**
	 * Specifies whether the quantity should be negated when it is applied. This allows sub-classes
	 * to change the default adjustment behavior.
	 * 
	 * @return
	 */
	protected boolean negateAppliedQuantity() {
		// Note that the quantity is NOT negated because the adjustment reservation quantity
		// is the difference
		return false;
	}
	
	@Override
	public boolean shouldVerifyNegativeStockAmountsAtSource() {
		return false;
	}
	
	@Override
	public BigDecimal getQuantityToApplyAtSource(BigDecimal quantity) {
		return quantity;
	}
	
	@Override
	public void onPending(final StockOperation operation) {
		executeCopyReserved(operation, new Action2<ReservedTransaction, StockItemTransaction>() {
			
			@Override
			public void apply(ReservedTransaction reserved, StockItemTransaction tx) {
				tx.setParty(operation.getSource());
				tx.setQuantity(tx.getQuantity());
				
			}
		});
	}
	
	@Override
	public void onCancelled(final StockOperation operation) {
		executeCopyReservedAndClear(operation, new Action2<ReservedTransaction, StockItemTransaction>() {
			
			@Override
			public void apply(ReservedTransaction reserved, StockItemTransaction tx) {
				tx.setParty(operation.getSource());
				
				// Undo the previously applied transaction by setting
				// the quantity to the opposite of the pending
				// transaction
				tx.setQuantity(tx.getQuantity().multiply(BigDecimal.valueOf(-1)));
				
			}
		});
	}
	
	@Override
	public void onCompleted(StockOperation operation) {
		// Clear out the transactions for the operation
		clearReservedTransactions(operation);
	}
}
