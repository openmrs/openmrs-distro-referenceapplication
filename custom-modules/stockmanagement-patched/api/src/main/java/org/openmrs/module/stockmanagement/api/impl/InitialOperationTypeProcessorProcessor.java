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
 * The initial operation type is used to populate a stockroom with the initial set of item stock at
 * the time when the system is first set up. It is currently implemented as a distinct class that
 * inherits the behavior of the receipt operation type however it may need to add it's own
 * implementation if we extend the behavior of receipt operations.
 */
public class InitialOperationTypeProcessorProcessor extends StockOperationTypeProcessorBase {
	
	public InitialOperationTypeProcessorProcessor(StockOperationType stockOperationType) {
		super(stockOperationType);
	}
	
	public boolean isNegativeItemQuantityAllowed() {
		return false;
	}
	
	@Override
	public boolean requiresActualBatchInformation() {
		return true;
	}
	
	@Override
	public boolean requiresBatchUuid() {
		return false;
	}
	
	@Override
	public boolean canCapturePurchasePrice() {
		return true;
	}
	
	@Override
	public boolean shouldVerifyNegativeStockAmountsAtSource() {
		return false;
	}
	
	@Override
	public void onPending(StockOperation operation) {
	}
	
	@Override
	public BigDecimal getQuantityToApplyAtSource(BigDecimal quantity) {
		return BigDecimal.valueOf(0);
	}
	
	@Override
	public void onCancelled(StockOperation operation) {
		// Clear out the transactions for the operation
		clearReservedTransactions(operation);
	}
	
	@Override
	public void onCompleted(final StockOperation operation) {
		executeCopyReservedAndClear(operation, new Action2<ReservedTransaction, StockItemTransaction>() {
			
			@Override
			public void apply(ReservedTransaction reserved, StockItemTransaction tx) {
				tx.setParty(operation.getSource());
			}
		});
	}
}
