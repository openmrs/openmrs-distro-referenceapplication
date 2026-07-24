package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.module.stockmanagement.api.IPagingInfo;
import org.openmrs.module.stockmanagement.api.PagingInfoBase;

import java.util.List;

public class Result<T> extends PagingInfoBase implements IPagingInfo {
	
	private List<T> data;
	
	private Integer page;
	
	private Integer pageSize;
	
	private Long totalRecordCount;
	
	private boolean loadRecordCount;
	
	public Result() {
	}
	
	public Result(List<T> data, long totalRecordCount) {
		this.data = data;
		this.totalRecordCount = totalRecordCount;
	}
	
	public List<T> getData() {
		return data;
	}
	
	public void setData(List<T> data) {
		this.data = data;
	}
	
	public Integer getPageIndex() {
		return page;
	}
	
	public void setPageIndex(Integer page) {
		this.page = page;
		loadRecordCount = true;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		loadRecordCount = true;
		this.pageSize = pageSize;
	}
	
	public Long getTotalRecordCount() {
		return totalRecordCount;
	}
	
	public void setTotalRecordCount(Long totalRecordCount) {
		this.totalRecordCount = totalRecordCount;
		
		// If the total records is set to anything other than null, than don't reload the count
		this.loadRecordCount = totalRecordCount == null;
	}
	
	public boolean shouldLoadRecordCount() {
		return loadRecordCount;
	}
	
	public void setLoadRecordCount(boolean loadRecordCount) {
		this.loadRecordCount = loadRecordCount;
	}
	
	@Override
	public void copyPagingInfoTo(IPagingInfo pagingInfo) {
		pagingInfo.setPageSize(getPageSize());
		pagingInfo.setPageIndex(getPageIndex());
		pagingInfo.setTotalRecordCount(getTotalRecordCount());
	}
	
}
