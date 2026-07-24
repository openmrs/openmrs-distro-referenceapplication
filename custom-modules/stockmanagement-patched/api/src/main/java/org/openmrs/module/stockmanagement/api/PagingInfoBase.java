package org.openmrs.module.stockmanagement.api;

public abstract class PagingInfoBase implements IPagingInfo {
	
	public Boolean hasMoreResults() {
		return getPageIndex() != null && getPageSize() != null && getTotalRecordCount() != null
		        && ((getPageIndex() + 1) * getPageSize()) < getTotalRecordCount();
	}
}
