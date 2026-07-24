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

import java.util.Date;

/**
 * The allowable {@link StockOperation} statuses.
 */
public enum StockOperationStatus {
	/**
	 * The operation is being created but has not yet been submitted.
	 */
	NEW(),
	/**
	 * The operation has been submitted but not yet approved.
	 */
	SUBMITTED(),
	
	/**
	 * The operation has been dispatched but items not yet received.
	 */
	DISPATCHED(),
	/**
	 * The operation has been returned for modification.
	 */
	RETURNED(),
	
	/**
	 * The operation has been rejected by the approver.
	 */
	REJECTED(),
	/**
	 * The operation was cancelled and the pending transactions were reversed.
	 */
	CANCELLED(),
	/**
	 * The operation was completed and the pending transactions were applied.
	 */
	COMPLETED();
	
	StockOperationStatus() {
	}
	
	public static boolean IsUpdateable(StockOperationStatus stockOperationStatus) {
		return stockOperationStatus != null && (stockOperationStatus == NEW || stockOperationStatus == RETURNED);
	}
	
	public static boolean IsApproveable(StockOperationStatus stockOperationStatus) {
		return stockOperationStatus != null && (stockOperationStatus == SUBMITTED);
	}
	
	public static boolean canReceiveItems(StockOperationStatus stockOperationStatus) {
		return stockOperationStatus != null && (stockOperationStatus == DISPATCHED);
	}
	
	public static boolean canDisplayReceivedItems(StockOperationStatus stockOperationStatus,
	        Date stockOperationDispatchedDate) {
		return stockOperationStatus != null
		        && (stockOperationStatus == DISPATCHED || ((stockOperationStatus == RETURNED || stockOperationStatus == COMPLETED) && stockOperationDispatchedDate != null)
		        
		        );
	}
	
	public static boolean isCompleted(StockOperationStatus stockOperationStatus) {
		return stockOperationStatus != null && stockOperationStatus == COMPLETED;
	}
	
	public static boolean canUpdateBatchInformation(StockOperationStatus stockOperationStatus) {
		return (!IsUpdateable(stockOperationStatus))
		        && (isCompleted(stockOperationStatus) || IsApproveable(stockOperationStatus));
	}
}
