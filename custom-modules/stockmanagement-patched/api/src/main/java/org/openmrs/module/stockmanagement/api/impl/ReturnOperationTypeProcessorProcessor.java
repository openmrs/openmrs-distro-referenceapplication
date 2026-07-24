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
 * The returned operation type is for items that get added back into the system as the result of a
 * needing to return the items. Only the items that are put back into stock should be part of this
 * operation. Functionally it is the same as a receipt operation.
 */
public class ReturnOperationTypeProcessorProcessor extends StockOperationTypeProcessorBase {
	
	public ReturnOperationTypeProcessorProcessor(StockOperationType stockOperationType) {
		super(stockOperationType);
	}
	
	@Override
	public boolean requiresActualBatchInformation() {
		return false;
	}
	
	@Override
	public boolean requiresBatchUuid() {
		return true;
	}
	
	@Override
	public boolean canCapturePurchasePrice() {
		return false;
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
	public boolean requiresDispatchAcknowledgement() {
		return true;
	}
	
	@Override
	public BigDecimal getQuantityToApplyAtSource(BigDecimal quantity) {
		return quantity.multiply(BigDecimal.valueOf(-1));
	}
	
	@Override
	public void onPending(final StockOperation operation) {
		// Remove the item stock from the source party
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
		// Re-add the previously removed item stock back into the source
		// party
		executeCopyReservedAndClear(operation, new Action2<ReservedTransaction, StockItemTransaction>() {
			
			@Override
			public void apply(ReservedTransaction reserved, StockItemTransaction tx) {
				tx.setParty(operation.getSource());
			}
		});
	}
	
	@Override
	public void onCompleted(final StockOperation operation) {
		// Add the item stock to the destination party
		executeCopyReservedAndClear(operation, new Action2<ReservedTransaction, StockItemTransaction>() {
			
			@Override
			public void apply(ReservedTransaction reserved, StockItemTransaction tx) {
				tx.setQuantity(reserved.getStockOperationItem().getQuantityReceived());
				tx.setStockItemPackagingUOM(reserved.getStockOperationItem().getQuantityReceivedPackagingUOM());
				tx.setParty(operation.getDestination());
			}
		});
	}
	
}
