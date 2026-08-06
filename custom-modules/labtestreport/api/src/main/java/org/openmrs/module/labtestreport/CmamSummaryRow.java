package org.openmrs.module.labtestreport;

/**
 * One (dimension, category) count for the CMAM Follow-up summary: how many children's most
 * recent CMAM encounter in the selected date range has this category for this dimension.
 * dimension is one of "currentDiagnosis", "childLastStatus", "alertStatus".
 */
public class CmamSummaryRow {

	private String dimension;

	private Integer categoryConceptId;

	private String category;

	private long total;

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public Integer getCategoryConceptId() {
		return categoryConceptId;
	}

	public void setCategoryConceptId(Integer categoryConceptId) {
		this.categoryConceptId = categoryConceptId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
