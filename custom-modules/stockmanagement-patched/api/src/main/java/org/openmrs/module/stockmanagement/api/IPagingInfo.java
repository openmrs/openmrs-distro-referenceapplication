package org.openmrs.module.stockmanagement.api;

public interface IPagingInfo {
	
	Integer getPageIndex();
	
	void setPageIndex(Integer pageIndexe);
	
	Integer getPageSize();
	
	void setPageSize(Integer pageSize);
	
	Long getTotalRecordCount();
	
	void setTotalRecordCount(Long totalRecordCount);
	
	boolean shouldLoadRecordCount();
	
	void setLoadRecordCount(boolean loadRecordCount);
	
	Boolean hasMoreResults();
	
	void copyPagingInfoTo(IPagingInfo pagingInfo);
}
