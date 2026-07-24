package org.openmrs.module.stockmanagement.api.impl;

import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.Privileges;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.StockOperationTypeProcessor;
import org.openmrs.module.stockmanagement.api.utils.Action2;
import org.openmrs.module.stockmanagement.api.model.*;

import java.util.Set;
import java.util.TreeSet;

public abstract class StockOperationTypeProcessorBase implements StockOperationTypeProcessor {
	
	protected StockOperationType stockOperationType;
	
	public StockOperationTypeProcessorBase(StockOperationType stockOperationType) {
		this.stockOperationType = stockOperationType;
	}
	
	@Override
	public boolean userCanProcess(User currentUser, Location location) {
		return userCanProcess(currentUser, location, Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE);
	}
	
	@Override
	public boolean userCanProcess(User user, Location location, String privilege) {
		return Context.getService(StockManagementService.class).userHasStockManagementPrivilege(
		    Context.getAuthenticatedUser(), location, this.stockOperationType, privilege);
	}
	
	@Override
	public boolean requiresReason() {
		return false;
	}
	
	@Override
	public boolean requiresBatchUuid() {
		return true;
	}
	
	@Override
	public boolean requiresActualBatchInformation() {
		return false;
	}
	
	@Override
	public boolean requiresDispatchAcknowledgement() {
		return false;
	}
	
	@Override
	public boolean isQuantityOptional() {
		return false;
	}
	
	@Override
	public boolean canBeRelatedToRequisition() {
		return false;
	}
	
	@Override
	public boolean canCapturePurchasePrice() {
		return false;
	}
	
	protected Set<StockItemTransaction> executeCopyReserved(StockOperation operation,
	        Action2<ReservedTransaction, StockItemTransaction> action) {
		Set<StockItemTransaction> transactions = new TreeSet<StockItemTransaction>();
		// Loop through the reserved transactions
		for (ReservedTransaction inTransit : operation.getReservedTransactions()) {
			// Create a new operation transaction as a copy of the reserved
			// transaction
			StockItemTransaction tx = new StockItemTransaction(inTransit);
			
			// Apply the action
			action.apply(inTransit, tx);
			
			// Add the operation transaction to the operation
			operation.addStockItemTransaction(tx);
			
			// Add the operation transaction to the copied transaction list
			transactions.add(tx);
		}
		
		return transactions;
	}
	
	protected Set<StockItemTransaction> executeCopyReservedAndClear(StockOperation operation,
	        Action2<ReservedTransaction, StockItemTransaction> action) {
		Set<StockItemTransaction> result = executeCopyReserved(operation, action);
		// Clear out the transactions for the operation
		clearReservedTransactions(operation);
		
		return result;
	}
	
	protected void clearReservedTransactions(StockOperation operation) {
		Context.getService(StockManagementService.class).deleteReservedTransations(operation.getId());
		operation.getReservedTransactions().clear();
		
	}
}
