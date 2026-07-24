package org.openmrs.module.stockmanagement.api.dto;

import java.util.List;

public class StockItemReferenceSearchFilter {
	
	private String uuid;
	
	private List<String> stockItemUuids;
	
	private List<Integer> stockItemIds;
	
	private boolean includeVoided;
	
	private boolean includeDispensingUnit;
	
	private Integer startIndex;
	
	private Integer limit;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public List<String> getStockItemUuids() {
		return stockItemUuids;
	}
	
	public void setStockItemUuids(List<String> stockItemUuids) {
		this.stockItemUuids = stockItemUuids;
	}
	
	public List<Integer> getStockItemIds() {
		return stockItemIds;
	}
	
	public void setStockItemIds(List<Integer> stockItemIds) {
		this.stockItemIds = stockItemIds;
	}
	
	public boolean isIncludeVoided() {
		return includeVoided;
	}
	
	public boolean getIncludeVoided() {
		return includeVoided;
	}
	
	public void setIncludeVoided(boolean includeVoided) {
		this.includeVoided = includeVoided;
	}
	
	public Integer getStartIndex() {
		return startIndex;
	}
	
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	
	public Integer getLimit() {
		return limit;
	}
	
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	
	public boolean includingDispensingUnit() {
		return includeDispensingUnit;
	}
	
	public void setIncludeDispensingUnit(boolean includeDispensingUnit) {
		this.includeDispensingUnit = includeDispensingUnit;
	}
	
	public static class ItemGroupFilter {
		
		private Integer stockItemId;
		
		private List<Integer> packagingUomIds;
		
		public ItemGroupFilter() {
		}
		
		public ItemGroupFilter(Integer stockItemId, List<Integer> packagingUomIds) {
			this.stockItemId = stockItemId;
			this.packagingUomIds = packagingUomIds;
		}
		
		public Integer getStockItemId() {
			return stockItemId;
		}
		
		public void setStockItemId(Integer stockItemId) {
			this.stockItemId = stockItemId;
		}
		
		public List<Integer> getPackagingUomIds() {
			return packagingUomIds;
		}
		
		public void setPackagingUomIds(List<Integer> packagingUomIds) {
			this.packagingUomIds = packagingUomIds;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			
			ItemGroupFilter that = (ItemGroupFilter) o;
			
			if (stockItemId != null ? !stockItemId.equals(that.stockItemId) : that.stockItemId != null)
				return false;
			return !(packagingUomIds != null ? !packagingUomIds.equals(that.packagingUomIds) : that.packagingUomIds != null);
			
		}
		
		@Override
		public int hashCode() {
			int result = stockItemId != null ? stockItemId.hashCode() : 0;
			result = 31 * result + (packagingUomIds != null ? packagingUomIds.hashCode() : 0);
			return result;
		}
	}
}
