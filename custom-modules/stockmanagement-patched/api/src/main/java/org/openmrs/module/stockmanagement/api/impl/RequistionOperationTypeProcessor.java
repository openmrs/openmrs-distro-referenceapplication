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

import org.openmrs.module.stockmanagement.api.model.StockOperation;
import org.openmrs.module.stockmanagement.api.model.StockOperationType;

import java.math.BigDecimal;

/**
 * Model class that processes a transfer stock operation type. Transfers move item stock from one
 * location to another.
 */
public class RequistionOperationTypeProcessor extends StockOperationTypeProcessorBase {
	
	public RequistionOperationTypeProcessor(StockOperationType stockOperationType) {
		super(stockOperationType);
	}
	
	@Override
	public boolean isNegativeItemQuantityAllowed() {
		return false;
	}
	
	@Override
	public boolean requiresActualBatchInformation() {
		return false;
	}
	
	@Override
	public boolean requiresBatchUuid() {
		return false;
	}
	
	@Override
	public boolean isQuantityOptional() {
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
	public void onPending(final StockOperation operation) {
		// Remove the item stock from the source stockroom
		if (operation.getReservedTransactions() != null) {
			clearReservedTransactions(operation);
		}
	}
	
	@Override
	public void onCancelled(final StockOperation operation) {
		// Re-add the previously removed item stock back into the source
		// stockroom
		if (operation.getReservedTransactions() != null) {
			clearReservedTransactions(operation);
		}
	}
	
	@Override
	public void onCompleted(final StockOperation operation) {
		if (operation.getReservedTransactions() != null) {
			clearReservedTransactions(operation);
		}
	}
}
