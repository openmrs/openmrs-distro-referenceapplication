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
 * Model class that represents a distribution stock operation type. Distribution operations remove
 * item stock from the system and record who received it.
 */
public class TransferOutOperationTypeProcessor extends StockOperationTypeProcessorBase {
	
	public TransferOutOperationTypeProcessor(StockOperationType stockOperationType) {
		super(stockOperationType);
	}
	
	@Override
	public boolean isNegativeItemQuantityAllowed() {
		return false;
	}
	
	@Override
	public boolean shouldVerifyNegativeStockAmountsAtSource() {
		return true;
	}
	
	@Override
	public BigDecimal getQuantityToApplyAtSource(BigDecimal quantity) {
		return quantity.multiply(BigDecimal.valueOf(-1));
	}
	
	@Override
	public void onPending(final StockOperation operation) {
		executeCopyReserved(operation, new Action2<ReservedTransaction, StockItemTransaction>() {
			
			@Override
			public void apply(ReservedTransaction reserved, StockItemTransaction tx) {
				tx.setParty(operation.getSource());
				// Negate the quantity because the item stock needs to
				// be removed from the source party
				tx.setQuantity(getQuantityToApplyAtSource(tx.getQuantity()));
			}
		});
	}
	
	@Override
	public void onCancelled(final StockOperation operation) {
		executeCopyReservedAndClear(operation, new Action2<ReservedTransaction, StockItemTransaction>() {
			
			@Override
			public void apply(ReservedTransaction reserved, StockItemTransaction tx) {
				tx.setParty(operation.getSource());
			}
		});
	}
	
	@Override
	public void onCompleted(StockOperation operation) {
		// Add the item stock to the destination party
		executeCopyReservedAndClear(operation, new Action2<ReservedTransaction, StockItemTransaction>() {
			
			@Override
			public void apply(ReservedTransaction reserved, StockItemTransaction tx) {
				tx.setParty(operation.getDestination());
			}
		});
	}
}
