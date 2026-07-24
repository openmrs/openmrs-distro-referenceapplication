package org.openmrs.module.stockmanagement.api.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockItemInventorySearchFilter {
	
	public enum InventoryMode {
		Total(), Consumption(), MostMoving(), LeastMoving()
	}
	
	public enum InventoryGroupBy {
		LocationStockItemBatchNo(), LocationStockItem(), StockItemOnly();
		
		public static InventoryGroupBy findByName(String name) {
			return findInList(name, values());
		}
		
		public static InventoryGroupBy findInList(String name, InventoryGroupBy[] parameterList) {
			InventoryGroupBy result = null;
			for (InventoryGroupBy enumValue : parameterList) {
				if (enumValue.name().equalsIgnoreCase(name)) {
					result = enumValue;
					break;
				}
			}
			return result;
		}
	}
	
	public StockItemInventorySearchFilter() {
		doSetPartyNameField = false;
		inventoryGroupBy = InventoryGroupBy.LocationStockItemBatchNo;
		includeStrength = false;
		includeConceptRefIds = false;
		isDispensing = false;
		includeStockItemName = false;
		allowEmptyBatchInfo = false;
		requireNonExpiredStockBatches = true;
		requireItemGroupFilters = true;
		inventoryMode = InventoryMode.Total;
	}
	
	private List<ItemGroupFilter> itemGroupFilters;
	
	private Integer startIndex;
	
	private boolean isDispensing;
	
	private boolean includeStrength;
	
	private boolean includeConceptRefIds;
	
	private Integer limit;
	
	private boolean doSetPartyNameField;
	
	private boolean doSetBatchFields;
	
	private boolean doSetQuantityUoM;
	
	private InventoryGroupBy inventoryGroupBy;
	
	private InventoryGroupBy totalBy;
	
	private boolean includeStockItemName;
	
	private Date date;
	
	private boolean allowEmptyBatchInfo;
	
	private Integer emptyBatchPartyId;
	
	private Boolean requireNonExpiredStockBatches;
	
	private List<Integer> unRestrictedPartyIds;
	
	private boolean requireItemGroupFilters;
	
	private Integer stockItemCategoryConceptId;
	
	private InventoryMode inventoryMode;
	
	private Date endDate;
	
	private Date startDate;
	
	public boolean includingConceptRefIds() {
		return includeConceptRefIds;
	}
	
	public void setIncludeConceptRefIds(boolean includeConceptRefIds) {
		this.includeConceptRefIds = includeConceptRefIds;
	}
	
	public boolean includingStrength() {
		return includeStrength;
	}
	
	public void setIncludeStrength(boolean includeStrength) {
		this.includeStrength = includeStrength;
	}
	
	public boolean dispensing() {
		return isDispensing;
	}
	
	public void setIsDispensing(boolean isDispensing) {
		this.isDispensing = isDispensing;
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
	
	public boolean getDoSetPartyNameField() {
		return doSetPartyNameField;
	}
	
	public void setDoSetPartyNameField(boolean doSetPartyNameField) {
		this.doSetPartyNameField = doSetPartyNameField;
	}
	
	public boolean getDoSetBatchFields() {
		return doSetBatchFields;
	}
	
	public void setDoSetBatchFields(boolean doSetBatchFields) {
		this.doSetBatchFields = doSetBatchFields;
	}
	
	public List<ItemGroupFilter> getItemGroupFilters() {
		return itemGroupFilters;
	}
	
	public void setItemGroupFilters(List<ItemGroupFilter> itemGroupFilters) {
		this.itemGroupFilters = itemGroupFilters;
	}
	
	public InventoryGroupBy getInventoryGroupBy() {
		return inventoryGroupBy;
	}
	
	public void setInventoryGroupBy(InventoryGroupBy inventoryGroupBy) {
		this.inventoryGroupBy = inventoryGroupBy;
	}
	
	public boolean getDoSetQuantityUoM() {
		return doSetQuantityUoM;
	}
	
	public void setDoSetQuantityUoM(boolean doSetQuantityUoM) {
		this.doSetQuantityUoM = doSetQuantityUoM;
	}
	
	public InventoryGroupBy getTotalBy() {
		return totalBy;
	}
	
	public void setTotalBy(InventoryGroupBy totalBy) {
		this.totalBy = totalBy;
	}
	
	public boolean isIncludeStockItemName() {
		return includeStockItemName;
	}
	
	public void setIncludeStockItemName(boolean includeStockItemName) {
		this.includeStockItemName = includeStockItemName;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public boolean getAllowEmptyBatchInfo() {
		return allowEmptyBatchInfo;
	}
	
	public void setAllowEmptyBatchInfo(boolean allowEmptyBatchInfo) {
		this.allowEmptyBatchInfo = allowEmptyBatchInfo;
	}
	
	public Integer getEmptyBatchPartyId() {
		return emptyBatchPartyId;
	}
	
	public void setEmptyBatchPartyId(Integer emptyBatchPartyId) {
		this.emptyBatchPartyId = emptyBatchPartyId;
	}
	
	public Boolean getRequireNonExpiredStockBatches() {
		return requireNonExpiredStockBatches;
	}
	
	public void setRequireNonExpiredStockBatches(Boolean requireNonExpiredStockBatches) {
		this.requireNonExpiredStockBatches = requireNonExpiredStockBatches;
	}
	
	public List<Integer> getUnRestrictedPartyIds() {
		return unRestrictedPartyIds;
	}
	
	public void setUnRestrictedPartyIds(List<Integer> unRestrictedPartyIds) {
		this.unRestrictedPartyIds = unRestrictedPartyIds;
	}
	
	public boolean isRequireItemGroupFilters() {
		return requireItemGroupFilters;
	}
	
	public void setRequireItemGroupFilters(boolean requireItemGroupFilters) {
		this.requireItemGroupFilters = requireItemGroupFilters;
	}
	
	public Integer getStockItemCategoryConceptId() {
		return stockItemCategoryConceptId;
	}
	
	public void setStockItemCategoryConceptId(Integer stockItemCategoryConceptId) {
		this.stockItemCategoryConceptId = stockItemCategoryConceptId;
	}
	
	public InventoryMode getInventoryMode() {
		return inventoryMode;
	}
	
	public void setInventoryMode(InventoryMode inventoryMode) {
		this.inventoryMode = inventoryMode;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public static class ItemGroupFilter {
		
		private List<Integer> partyIds;
		
		private List<String> partyUuids;
		
		private String stockItemUuid;
		
		private Integer stockItemId;
		
		private List<Integer> stockBatchIds;
		
		public ItemGroupFilter() {
		}
		
		public ItemGroupFilter(List<Integer> partyIds, Integer stockItemId, Integer stockBatchId) {
			this.partyIds = partyIds;
			this.stockItemId = stockItemId;
			if(stockBatchId != null) {
				this.stockBatchIds = new ArrayList<>();
				this.stockBatchIds.add(stockBatchId);
			}
		}
		
		public ItemGroupFilter(Integer stockItemId, List<String> partyUuids, Integer stockBatchId) {
			this.partyUuids = partyUuids;
			this.stockItemId = stockItemId;
			if(stockBatchId != null) {
				this.stockBatchIds = new ArrayList<>();
				this.stockBatchIds.add(stockBatchId);
			}
		}
		
		public ItemGroupFilter(Integer stockItemId, List<String> partyUuids, List<Integer> stockBatchIds) {
			this.partyUuids = partyUuids;
			this.stockItemId = stockItemId;
			this.stockBatchIds = stockBatchIds;
		}
		
		public List<Integer> getPartyIds() {
			return partyIds;
		}
		
		public void setPartyIds(List<Integer> partyIds) {
			this.partyIds = partyIds;
		}
		
		public List<String> getPartyUuids() {
			return partyUuids;
		}
		
		public void setPartyUuids(List<String> partyUuids) {
			this.partyUuids = partyUuids;
		}
		
		public String getStockItemUuid() {
			return stockItemUuid;
		}
		
		public void setStockItemUuid(String stockItemUuid) {
			this.stockItemUuid = stockItemUuid;
		}
		
		public Integer getStockItemId() {
			return stockItemId;
		}
		
		public void setStockItemId(Integer stockItemId) {
			this.stockItemId = stockItemId;
		}
		
		public List<Integer> getStockBatchIds() {
			return stockBatchIds;
		}
		
		public Integer getFirstStockBatchId() {
			return stockBatchIds == null || stockBatchIds.isEmpty() ? null : stockBatchIds.get(0);
		}
		
		public void setStockBatchIds(List<Integer> stockBatchIds) {
			this.stockBatchIds = stockBatchIds;
		}
		
	}
	
	public static class PartyStockItemBatch {
		
		private Integer partyId;
		
		private Integer stockItemId;
		
		private Integer stockBatchId;
		
		public PartyStockItemBatch() {
		}
		
		public PartyStockItemBatch(Integer partyId, Integer stockItemId, Integer stockBatchId) {
			this.partyId = partyId;
			this.stockItemId = stockItemId;
			this.stockBatchId = stockBatchId;
		}
		
		public Integer getPartyId() {
			return partyId;
		}
		
		public void setPartyId(Integer partyId) {
			this.partyId = partyId;
		}
		
		public Integer getStockItemId() {
			return stockItemId;
		}
		
		public void setStockItemId(Integer stockItemId) {
			this.stockItemId = stockItemId;
		}
		
		public Integer getStockBatchId() {
			return stockBatchId;
		}
		
		public void setStockBatchId(Integer stockBatchId) {
			this.stockBatchId = stockBatchId;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			
			PartyStockItemBatch that = (PartyStockItemBatch) o;
			
			if (partyId != null ? !partyId.equals(that.partyId) : that.partyId != null)
				return false;
			if (stockItemId != null ? !stockItemId.equals(that.stockItemId) : that.stockItemId != null)
				return false;
			return !(stockBatchId != null ? !stockBatchId.equals(that.stockBatchId) : that.stockBatchId != null);
			
		}
		
		@Override
		public int hashCode() {
			int result = partyId != null ? partyId.hashCode() : 0;
			result = 31 * result + (stockItemId != null ? stockItemId.hashCode() : 0);
			result = 31 * result + (stockBatchId != null ? stockBatchId.hashCode() : 0);
			return result;
		}
		
	}
}
