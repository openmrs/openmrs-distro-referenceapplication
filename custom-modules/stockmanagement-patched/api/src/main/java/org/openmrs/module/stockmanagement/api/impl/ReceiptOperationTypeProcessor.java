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
import org.openmrs.module.stockmanagement.api.model.*;

import java.math.BigDecimal;

/**
 * Model class that represents a receipt stock operation type. Receipt operations bring new item
 * stock into the system.
 */

public class ReceiptOperationTypeProcessor extends StockOperationTypeProcessorBase {
	
	public ReceiptOperationTypeProcessor(StockOperationType stockOperationType) {
		super(stockOperationType);
	}
	
	@Override
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
	public BigDecimal getQuantityToApplyAtSource(BigDecimal quantity) {
		return BigDecimal.valueOf(0);
	}
	
	@Override
	public void onPending(StockOperation operation) {
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
				tx.setParty(operation.getDestination());
			}
		});
	}
}
