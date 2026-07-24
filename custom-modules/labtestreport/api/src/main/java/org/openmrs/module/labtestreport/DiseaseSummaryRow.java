package org.openmrs.module.labtestreport;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * One row of the disease surveillance summary report: a single diagnosis within a category, with
 * its age-group/gender breakdown. {@link #categoryRowSpan} is a presentation-only value filled in
 * by the controller so the JSP/JSON consumer can merge consecutive rows that share a category.
 */
public class DiseaseSummaryRow {

	private Integer categoryConceptId;

	private String category;

	private int categoryRowSpan = 0;

	private Integer diagnosisConceptId;

	private String diagnosisLabel;

	private long totalCases;

	private final Map<String, Long> counts = new LinkedHashMap<>();

	private long total;

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

	public int getCategoryRowSpan() {
		return categoryRowSpan;
	}

	public void setCategoryRowSpan(int categoryRowSpan) {
		this.categoryRowSpan = categoryRowSpan;
	}

	public Integer getDiagnosisConceptId() {
		return diagnosisConceptId;
	}

	public void setDiagnosisConceptId(Integer diagnosisConceptId) {
		this.diagnosisConceptId = diagnosisConceptId;
	}

	public String getDiagnosisLabel() {
		return diagnosisLabel;
	}

	public void setDiagnosisLabel(String diagnosisLabel) {
		this.diagnosisLabel = diagnosisLabel;
	}

	public long getTotalCases() {
		return totalCases;
	}

	public void setTotalCases(long totalCases) {
		this.totalCases = totalCases;
	}

	public Map<String, Long> getCounts() {
		return counts;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
