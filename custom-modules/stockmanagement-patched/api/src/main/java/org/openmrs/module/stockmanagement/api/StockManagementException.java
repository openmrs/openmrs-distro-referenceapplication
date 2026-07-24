package org.openmrs.module.stockmanagement.api;

public class StockManagementException extends RuntimeException {
	
	public StockManagementException() {
	}
	
	public StockManagementException(String message) {
		super(message);
	}
	
	public StockManagementException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public StockManagementException(Throwable cause) {
		super(cause);
	}
	
	public StockManagementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
