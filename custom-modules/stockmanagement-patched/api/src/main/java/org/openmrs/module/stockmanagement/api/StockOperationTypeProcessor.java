package org.openmrs.module.stockmanagement.api;

import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.module.stockmanagement.api.model.StockOperation;

import java.math.BigDecimal;

public interface StockOperationTypeProcessor {
	
	boolean requiresReason();
	
	boolean userCanProcess(User user, Location location);
	
	boolean userCanProcess(User user, Location location, String privilege);
	
	boolean requiresBatchUuid();
	
	boolean requiresActualBatchInformation();
	
	boolean requiresDispatchAcknowledgement();
	
	boolean isQuantityOptional();
	
	boolean canBeRelatedToRequisition();
	
	boolean canCapturePurchasePrice();
	
	boolean shouldVerifyNegativeStockAmountsAtSource();
	
	BigDecimal getQuantityToApplyAtSource(BigDecimal quantity);
	
	/**
	 * Called when the {@link StockOperation} status is initially created and the status is
	 * StockOperationStatus.PENDING.
	 * 
	 * @param operation The associated stock operation.
	 */
	void onPending(StockOperation operation);
	
	/**
	 * Called when the {@link StockOperation} status is changed to StockOperationStatus.CANCELLED.
	 * 
	 * @param operation The associated stock operation.
	 */
	void onCancelled(StockOperation operation);
	
	/**
	 * Called when the {@link StockOperation} status is changed to StockOperationStatus.COMPLETED.
	 * 
	 * @param operation The associated stock operation.
	 */
	void onCompleted(StockOperation operation);
	
	/**
	 * Determines weather or not negative quantities for items are allowed
	 * 
	 * @return true if negative quantities are allowed, else false
	 */
	boolean isNegativeItemQuantityAllowed();
}
