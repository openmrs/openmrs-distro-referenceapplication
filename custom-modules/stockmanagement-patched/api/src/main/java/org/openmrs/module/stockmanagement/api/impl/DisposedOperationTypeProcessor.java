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

import org.openmrs.module.stockmanagement.api.model.StockOperationType;

/**
 * A disposed operation is for expired items that must be removed from circulation. While it could
 * technically be implemented as a form of an adjustment operation, it is notable enough (for
 * reporting) to warrant it's own type.
 */
public class DisposedOperationTypeProcessor extends AdjustmentOperationTypeProcessor {
	
	public DisposedOperationTypeProcessor(StockOperationType stockOperationType) {
		super(stockOperationType);
	}
	
	@Override
	protected boolean negateAppliedQuantity() {
		return true;
	}
	
	@Override
	public boolean isNegativeItemQuantityAllowed() {
		return false;
	}
}
