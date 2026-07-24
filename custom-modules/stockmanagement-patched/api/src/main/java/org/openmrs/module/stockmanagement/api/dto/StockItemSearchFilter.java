package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.User;

import java.util.List;

public class StockItemSearchFilter {
	
	private String uuid;
	
	private Integer startIndex;
	
	private Integer limit;
	
	private Boolean isDrug;
	
	private List<Drug> drugs;
	
	private List<Concept> concepts;
	
	private List<Integer> stockItemIds;
	
	private Integer drugId;
	
	private Integer conceptId;
	
	private boolean includeVoided;
	
	private boolean searchEitherDrugsOrConcepts = false;
	
	private List<Concept> categories;
	
	private Integer categoryId;
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	
	public Boolean getIsDrug() {
		return isDrug;
	}
	
	public void setIsDrug(Boolean isDrug) {
		this.isDrug = isDrug;
	}
	
	public List<Drug> getDrugs() {
		return drugs;
	}
	
	public void setDrugs(List<Drug> drugs) {
		this.drugs = drugs;
	}
	
	public List<Concept> getConcepts() {
		return concepts;
	}
	
	public void setConcepts(List<Concept> concepts) {
		this.concepts = concepts;
	}
	
	public boolean getIncludeVoided() {
		return includeVoided;
	}
	
	public void setIncludeVoided(boolean includeVoided) {
		this.includeVoided = includeVoided;
	}
	
	public boolean getSearchEitherDrugsOrConcepts() {
		return searchEitherDrugsOrConcepts;
	}
	
	public void setSearchEitherDrugsOrConcepts(boolean searchEitherDrugsOrConcepts) {
		this.searchEitherDrugsOrConcepts = searchEitherDrugsOrConcepts;
	}
	
	public List<Integer> getStockItemIds() {
		return stockItemIds;
	}
	
	public void setStockItemIds(List<Integer> stockItemIds) {
		this.stockItemIds = stockItemIds;
	}
	
	public Integer getDrugId() {
		return drugId;
	}
	
	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}
	
	public Integer getConceptId() {
		return conceptId;
	}
	
	public void setConceptId(Integer conceptId) {
		this.conceptId = conceptId;
	}
	
	public List<Concept> getCategories() {
		return categories;
	}
	
	public void setCategories(List<Concept> categories) {
		this.categories = categories;
	}
	
	public Integer getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	public static class ItemGroupFilter {
		
		private Integer drugId;
		
		private Integer conceptId;
		
		private Boolean isDrug;
		
		public Integer getDrugId() {
			return drugId;
		}
		
		public void setDrugId(Integer drugId) {
			this.drugId = drugId;
		}
		
		public Integer getConceptId() {
			return conceptId;
		}
		
		public void setConceptId(Integer conceptId) {
			this.conceptId = conceptId;
		}
		
		public Boolean getIsDrug() {
			return isDrug;
		}
		
		public void setIsDrug(Boolean isDrug) {
			this.isDrug = isDrug;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			
			ItemGroupFilter that = (ItemGroupFilter) o;
			
			if (drugId != null ? !drugId.equals(that.drugId) : that.drugId != null)
				return false;
			if (conceptId != null ? !conceptId.equals(that.conceptId) : that.conceptId != null)
				return false;
			return !(isDrug != null ? !isDrug.equals(that.isDrug) : that.isDrug != null);
			
		}
		
		@Override
		public int hashCode() {
			int result = drugId != null ? drugId.hashCode() : 0;
			result = 31 * result + (conceptId != null ? conceptId.hashCode() : 0);
			result = 31 * result + (isDrug != null ? isDrug.hashCode() : 0);
			return result;
		}
	}
}
